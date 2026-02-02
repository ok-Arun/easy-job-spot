package com.easyjobspot.backend.profile.security;

import com.easyjobspot.backend.profile.repository.JobSeekerProfileRepository;
import com.easyjobspot.backend.profile.repository.ProviderProfileRepository;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ProfileCompletionFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobRepo;
    private final ProviderProfileRepository providerRepo;

    public ProfileCompletionFilter(
            UserRepository userRepository,
            JobSeekerProfileRepository jobRepo,
            ProviderProfileRepository providerRepo
    ) {
        this.userRepository = userRepository;
        this.jobRepo = jobRepo;
        this.providerRepo = providerRepo;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        // allow auth + profile creation APIs
        return path.startsWith("/api/auth")
                || path.startsWith("/api/profile");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getUserPrincipal() == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = request.getUserPrincipal().getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // admin always allowed
        if (user.getUserType() == User.UserType.SYSTEM_ADMIN) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean completed = false;

        if (user.getUserType() == User.UserType.JOB_SEEKER) {
            completed = jobRepo.findByUserId(user.getId())
                    .map(p -> p.isProfileCompleted())
                    .orElse(false);
        }

        if (user.getUserType() == User.UserType.JOB_PROVIDER) {
            completed = providerRepo.findByUserId(user.getId())
                    .map(p -> p.isProfileCompleted())
                    .orElse(false);
        }

        if (!completed) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"message\":\"PROFILE_INCOMPLETE\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

package com.easyjobspot.backend.config;

import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdmin() {
        return args -> {

            String adminEmail = "admin@easyjobspot.com";
            String adminPassword = "12345678";

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = User.builder()
                    .name("System Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(User.Role.ADMIN)
                    .userType(User.UserType.SYSTEM_ADMIN)
                    .build();

            userRepository.save(admin);

            System.out.println(
                    "✅ Default admin created → email: "
                            + adminEmail
                            + " | password: "
                            + adminPassword
            );
        };
    }
}

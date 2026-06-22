package com.easyjobspot.backend.config;

import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.entity.Application;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import com.easyjobspot.backend.profile.entity.JobSeekerProfile;
import com.easyjobspot.backend.profile.entity.ProviderProfile;
import com.easyjobspot.backend.profile.repository.JobSeekerProfileRepository;
import com.easyjobspot.backend.profile.repository.ProviderProfileRepository;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private static final String DEFAULT_PASSWORD = "12345678";

    private static final String GOOGLE_EMAIL = "google.demo@easyjobspot.com";
    private static final String MICROSOFT_EMAIL = "microsoft.demo@easyjobspot.com";
    private static final String AMAZON_EMAIL = "amazon.demo@easyjobspot.com";

    private static final String ARUN_EMAIL = "arun.demo@easyjobspot.com";
    private static final String RAHUL_EMAIL = "rahul.demo@easyjobspot.com";
    private static final String PRIYA_EMAIL = "priya.demo@easyjobspot.com";
    private static final String NEHA_EMAIL = "neha.demo@easyjobspot.com";
    private static final String AMIT_EMAIL = "amit.demo@easyjobspot.com";

    private final UserRepository userRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        seedProviders();

        seedJobSeekers();

        seedProviderProfiles();

        seedJobSeekerProfiles();

        seedJobs();

        seedApplications();
    }

    /**
     * Demo providers
     */
    private void seedProviders() {

        createProvider(
                "Google Demo",
                GOOGLE_EMAIL
        );

        createProvider(
                "Microsoft Demo",
                MICROSOFT_EMAIL
        );

        createProvider(
                "Amazon Demo",
                AMAZON_EMAIL
        );
    }

    /**
     * Demo job seekers
     */
    private void seedJobSeekers() {

        createJobSeeker(
                "Arun Demo",
                ARUN_EMAIL
        );

        createJobSeeker(
                "Rahul Demo",
                RAHUL_EMAIL
        );

        createJobSeeker(
                "Priya Demo",
                PRIYA_EMAIL
        );

        createJobSeeker(
                "Neha Demo",
                NEHA_EMAIL
        );

        createJobSeeker(
                "Amit Demo",
                AMIT_EMAIL
        );
    }

    /**
     * Provider profiles
     */
    private void seedProviderProfiles() {

        createProviderProfile(
                GOOGLE_EMAIL,
                "Google India",
                GOOGLE_EMAIL,
                "+91-9876543210",
                "https://www.google.com",
                "Bengaluru, Karnataka",
                "Leading cloud and software company."
        );

        createProviderProfile(
                MICROSOFT_EMAIL,
                "Microsoft India",
                MICROSOFT_EMAIL,
                "+91-9876543211",
                "https://www.microsoft.com",
                "Hyderabad, Telangana",
                "Global technology company."
        );

        createProviderProfile(
                AMAZON_EMAIL,
                "Amazon India",
                AMAZON_EMAIL,
                "+91-9876543212",
                "https://www.amazon.com",
                "Bengaluru, Karnataka",
                "E-commerce and cloud services company."
        );
    }

    /**
     * Job seeker profiles
     */
    private void seedJobSeekerProfiles() {

        createJobSeekerProfile(
                ARUN_EMAIL,
                "Arun",
                "Mahto",
                "+91-9876543201",
                "Bengaluru",
                "Java, Spring Boot, MySQL",
                "2 Years",
                "BCA",
                "https://resume.easyjobspot.com/arun.pdf",
                "Java Backend Developer",
                "Full Time",
                "Bengaluru",
                "30 Days",
                "https://linkedin.com/in/arun",
                "https://github.com/arun"
        );

        createJobSeekerProfile(
                RAHUL_EMAIL,
                "Rahul",
                "Sharma",
                "+91-9876543202",
                "Hyderabad",
                "React, TypeScript, JavaScript",
                "1 Year",
                "B.Tech",
                "https://resume.easyjobspot.com/rahul.pdf",
                "Frontend Developer",
                "Full Time",
                "Hyderabad",
                "15 Days",
                "https://linkedin.com/in/rahul",
                "https://github.com/rahul"
        );

        createJobSeekerProfile(
                PRIYA_EMAIL,
                "Priya",
                "Verma",
                "+91-9876543203",
                "Pune",
                "Python, Django, PostgreSQL",
                "3 Years",
                "MCA",
                "https://resume.easyjobspot.com/priya.pdf",
                "Python Developer",
                "Full Time",
                "Pune",
                "30 Days",
                "https://linkedin.com/in/priya",
                "https://github.com/priya"
        );

        createJobSeekerProfile(
                NEHA_EMAIL,
                "Neha",
                "Singh",
                "+91-9876543204",
                "Noida",
                "QA, Selenium, JUnit",
                "2 Years",
                "B.Tech",
                "https://resume.easyjobspot.com/neha.pdf",
                "QA Engineer",
                "Full Time",
                "Noida",
                "15 Days",
                "https://linkedin.com/in/neha",
                "https://github.com/neha"
        );

        createJobSeekerProfile(
                AMIT_EMAIL,
                "Amit",
                "Kumar",
                "+91-9876543205",
                "Chennai",
                "AWS, Docker, Kubernetes",
                "4 Years",
                "B.Tech",
                "https://resume.easyjobspot.com/amit.pdf",
                "DevOps Engineer",
                "Full Time",
                "Chennai",
                "60 Days",
                "https://linkedin.com/in/amit",
                "https://github.com/amit"
        );
    }

    /**
     * Jobs
     */
    private void seedJobs() {

        userRepository.findByEmail(GOOGLE_EMAIL)
                .ifPresent(provider -> {

                    createJob(
                            provider,
                            "Java Backend Developer",
                            "Google India",
                            "Software Development",
                            "Bengaluru",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "Spring Boot Developer",
                            "Google India",
                            "Software Development",
                            "Hyderabad",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "SDE Intern",
                            "Google India",
                            "Software Development",
                            "Remote",
                            Job.JobType.INTERNSHIP
                    );
                });

        userRepository.findByEmail(MICROSOFT_EMAIL)
                .ifPresent(provider -> {

                    createJob(
                            provider,
                            "React Developer",
                            "Microsoft India",
                            "Frontend",
                            "Noida",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "Full Stack Developer",
                            "Microsoft India",
                            "Software Development",
                            "Hyderabad",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "Cloud Engineer",
                            "Microsoft India",
                            "Cloud",
                            "Remote",
                            Job.JobType.FULL_TIME
                    );
                });

        userRepository.findByEmail(AMAZON_EMAIL)
                .ifPresent(provider -> {

                    createJob(
                            provider,
                            "DevOps Engineer",
                            "Amazon India",
                            "DevOps",
                            "Chennai",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "Python Developer",
                            "Amazon India",
                            "Software Development",
                            "Pune",
                            Job.JobType.FULL_TIME
                    );

                    createJob(
                            provider,
                            "QA Engineer",
                            "Amazon India",
                            "Testing",
                            "Bengaluru",
                            Job.JobType.FULL_TIME
                    );
                });
    }

    /**
     * Applications
     */
    private void seedApplications() {

        createApplication(
                ARUN_EMAIL,
                "Java Backend Developer",
                ApplicationStatus.APPLIED
        );

        createApplication(
                RAHUL_EMAIL,
                "React Developer",
                ApplicationStatus.SHORTLISTED
        );

        createApplication(
                PRIYA_EMAIL,
                "Python Developer",
                ApplicationStatus.HIRED
        );

        createApplication(
                NEHA_EMAIL,
                "QA Engineer",
                ApplicationStatus.REJECTED
        );

        createApplication(
                AMIT_EMAIL,
                "DevOps Engineer",
                ApplicationStatus.SHORTLISTED
        );
    }


    // ====================================================
    // HELPER METHODS
    // ====================================================

    private void createProvider(
            String name,
            String email
    ) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User provider = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .role(User.Role.USER)
                .userType(User.UserType.JOB_PROVIDER)
                .providerStatus(User.ProviderStatus.APPROVED)
                .profileCompleted(true)
                .build();

        userRepository.save(provider);
    }

    private void createJobSeeker(
            String name,
            String email
    ) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User seeker = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .role(User.Role.USER)
                .userType(User.UserType.JOB_SEEKER)
                .profileCompleted(true)
                .build();

        userRepository.save(seeker);
    }

    private void createProviderProfile(
            String userEmail,
            String companyName,
            String companyEmail,
            String companyPhone,
            String website,
            String address,
            String description
    ) {

        userRepository.findByEmail(userEmail).ifPresent(user -> {

            if (providerProfileRepository.findByUserId(user.getId()).isPresent()) {
                return;
            }

            ProviderProfile profile = new ProviderProfile();

            profile.setUserId(user.getId());
            profile.setCompanyName(companyName);
            profile.setCompanyEmail(companyEmail);
            profile.setCompanyPhone(companyPhone);
            profile.setWebsite(website);
            profile.setAddress(address);
            profile.setDescription(description);
            profile.setApprovedAt(LocalDateTime.now());
            profile.setProfileCompleted(true);

            providerProfileRepository.save(profile);
        });
    }

    private void createJobSeekerProfile(
            String email,
            String firstName,
            String lastName,
            String phone,
            String location,
            String skills,
            String experience,
            String education,
            String resumeUrl,
            String currentJobTitle,
            String preferredJobType,
            String preferredLocation,
            String noticePeriod,
            String linkedinUrl,
            String portfolioUrl
    ) {

        userRepository.findByEmail(email).ifPresent(user -> {

            if (jobSeekerProfileRepository.findByUserId(user.getId()).isPresent()) {
                return;
            }

            JobSeekerProfile profile = new JobSeekerProfile();

            profile.setUserId(user.getId());
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setPhone(phone);
            profile.setLocation(location);
            profile.setSkills(skills);
            profile.setExperience(experience);
            profile.setEducation(education);
            profile.setResumeUrl(resumeUrl);
            profile.setCurrentJobTitle(currentJobTitle);
            profile.setPreferredJobType(preferredJobType);
            profile.setPreferredLocation(preferredLocation);
            profile.setNoticePeriod(noticePeriod);
            profile.setLinkedinUrl(linkedinUrl);
            profile.setPortfolioUrl(portfolioUrl);
            profile.setProfileCompleted(true);

            jobSeekerProfileRepository.save(profile);
        });
    }

    private void createJob(
            User provider,
            String title,
            String company,
            String category,
            String location,
            Job.JobType jobType
    ) {

        if (jobRepository
                .existsByTitleIgnoreCaseAndCompanyIgnoreCaseAndLocationIgnoreCaseAndJobTypeAndCreatedBy(
                        title,
                        company,
                        location,
                        jobType,
                        provider.getId()
                )) {
            return;
        }

        Job job = Job.builder()
                .title(title)
                .company(company)
                .category(category)
                .location(location)
                .jobType(jobType)
                .description("Demo job generated automatically.")
                .workMode("Hybrid")
                .employmentLevel("Mid Level")
                .salaryMin(600000.0)
                .salaryMax(1800000.0)
                .experienceMin(1)
                .experienceMax(5)
                .vacancyCount(3)
                .applicationType("EXTERNAL")
                .applicationUrl("https://easyjobspot.com")
                .deadline(LocalDateTime.now().plusMonths(1))
                .createdBy(provider.getId())
                .build();

        jobRepository.save(job);

        job.approve();

        jobRepository.save(job);
    }

    private void createApplication(
            String seekerEmail,
            String jobTitle,
            ApplicationStatus targetStatus
    ) {

        List<Job> jobs = jobRepository.findByTitleIgnoreCase(jobTitle);

        if (jobs.isEmpty()) {
            return;
        }

        Job job = jobs.get(0);
    }

}



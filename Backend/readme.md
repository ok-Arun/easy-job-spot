EasyJobSpot – Spring Boot Backend

EasyJobSpot is a role-based job portal backend built using Spring Boot.
The backend is designed around strict access control, ownership validation, and admin-controlled workflows.
Authentication and authorization are enforced using JWT, security filters, and service-layer business rules.

This backend represents real application behavior and production-style design, not a simple CRUD demo.

---

TECH STACK

Java 17
Spring Boot
Spring Security (JWT, Filters)
Spring Data JPA
Maven
SLF4J Logging
Relational Database (JPA-based)

---

BACKEND DESIGN

Controllers
Handle HTTP requests and define role boundaries. No business logic exists here.

Services
Contain all business rules, validations, and ownership checks.

Repositories
Responsible only for database access.

Security / Filters
Handle JWT authentication and provider profile enforcement before requests reach controllers.

Audit Layer
Tracks sensitive admin-level actions.

Core rule:
Business logic lives only in services, never in controllers.

---

SECURITY DESIGN

Authentication is stateless and JWT-based.
Authorization is enforced at two levels:

1. Security configuration and filters
2. Service-layer validations

Important security-related files:
JwtAuthenticationFilter.java
JwtTokenProvider.java
ProfileCompletionFilter.java
SecurityConfig.java

---

FEATURES (USER TYPE → FEATURE → API → ENFORCEMENT)

---

## JOB SEEKER FEATURES

View Job Details
A job seeker can view only approved jobs.

API
GET /api/jobs/{id}

Enforced in
JobService.java

if (!job.getStatus().equals(JobStatus.APPROVED)) {
throw new BadRequestException("Job not available");
}

---

Apply to Job (Only Once)
A job seeker can apply to a job only once and can view only their own applications.

APIs
POST /api/applications/{jobId}
GET  /api/applications/my

Enforced in
JobApplicationService.java

if (applicationRepository.existsByJobIdAndUserId(jobId, userId)) {
throw new BadRequestException("Already applied");
}

---

Job Seeker Profile
Job seekers can check profile status and update their profile details.

APIs
GET /api/profile/status
PUT /api/profile/job-seeker

---

## JOB PROVIDER FEATURES

Provider Profile (Mandatory)
A provider cannot post or manage jobs until their profile is completed.

API
PUT /api/profile/provider

Enforced in
ProfileCompletionFilter.java

if (!providerProfile.isCompleted()) {
throw new BadRequestException(
"COMPLETE_PROVIDER_PROFILE_BEFORE_POSTING_JOB"
);
}

---

Manage Own Jobs Only
A provider can update, close, or reopen only the jobs they created.

APIs
PUT /api/provider/jobs/{id}
PUT /api/provider/jobs/{id}/close
PUT /api/provider/jobs/{id}/reopen

Enforced in
JobService.java

if (!job.getCreatedBy().equals(currentUser.getId())) {
throw new AccessDeniedException("Not job owner");
}

---

Provider Dashboard
A provider can view statistics related to their own jobs and applications.

API
GET /api/provider/dashboard/stats

---

## ADMIN FEATURES

Provider Approval
Admins approve or reject provider accounts.

APIs
GET /api/admin/providers/pending
PUT /api/admin/providers/{id}/approve
PUT /api/admin/providers/{id}/reject

---

Job Moderation
Admins control the full job lifecycle.

APIs
GET /api/admin/jobs/pending
PUT /api/admin/jobs/{jobId}/approve
PUT /api/admin/jobs/{jobId}/reject
PUT /api/admin/jobs/{jobId}/close

Enforced in
JobService.java

if (!user.getRole().equals(Role.ADMIN)) {
throw new AccessDeniedException("Admin only");
}

---

View Applications (Any Job)
Admins can view applications for any job.

API
GET /api/admin/jobs/{jobId}/applications

---

System Dashboard
Admins can view system-wide statistics and trends.

APIs
GET /api/admin/dashboard/stats
GET /api/admin/dashboard/trends

---

CORE BUSINESS RULES

Provider profile must be completed before job posting
Duplicate job applications are blocked
Only the job owner or admin can modify a job
Only admins can delete jobs
Job lifecycle is strictly enforced:

PENDING → APPROVED → ACTIVE → CLOSED

---

GLOBAL JWT ENFORCEMENT

File
JwtAuthenticationFilter.java

SecurityContextHolder.getContext()
.setAuthentication(authentication);

---

ERROR HANDLING

Centralized exception handling is used.
Custom exceptions include:
BadRequestException
ResourceNotFoundException

Errors are returned with clean, consistent HTTP responses.

Handled in:
GlobalExceptionHandler.java

---

RUNNING THE BACKEND

mvn clean install
mvn spring-boot:run


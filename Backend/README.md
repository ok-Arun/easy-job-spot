# 🧠 EasyJobSpot — Spring Boot Backend

---

## 1. Project Overview

**EasyJobSpot Backend** is a role-driven job portal backend designed with **strict business rules**, **clear ownership boundaries**, and **audit-safe workflows**.

This backend intentionally enforces centralized authority and prevents self-approval or role abuse.

This backend enforces:

* Approval-based job publishing
* Role-isolated capabilities
* Profile-gated actions
* Admin-controlled ecosystem integrity

Every API exists for a **business reason**, not convenience.

---

## 2. Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT)**
* **Spring Data JPA**
* **PostgreSQL**
* **Hibernate**
* **Lombok**
* **Maven**

---

## 3. Backend Design (High-Level)

**Layered & Responsibility-Driven Architecture**

```
Controller → Service → Repository → Database
           ↓
         DTOs
```

**Key Design Decisions**

* Controllers are thin — zero business logic
* Services own validation, rules, and workflows
* DTOs isolate persistence from API contracts
* Authorization is enforced at **security + service layer**
* Admin actions are explicitly separated (no role overloading)

This design prevents logic leaks and privilege escalation.

---

## 4. Security Design

* **JWT-based stateless authentication**
* **Role-based authorization**

  * `USER`
  * `PROVIDER`
  * `ADMIN`
* Profile completion enforced **before privileged actions**
* Admin-only APIs isolated under `/api/admin/**`
* Providers cannot self-approve jobs or bypass moderation

Authorization is applied defensively, not annotation-only.

---

# 5. User Roles & Features

*(feature → API → rule → code reference where it adds value)*

---

## 🔵 USER (Job Seeker)

---

### 🔹 Authentication

**APIs**

* <span style="color:blue"><code>POST /api/auth/register</code></span>
* <span style="color:blue"><code>POST /api/auth/login</code></span>

**Rules**

* Registration creates a basic USER role
* JWT is mandatory for all protected APIs

---

### 🔹 Profile Management

**APIs**

* <span style="color:blue"><code>GET /api/profile/status</code></span>
* <span style="color:blue"><code>PUT /api/profile/job-seeker</code></span>

**Rules**

* Profile must be completed before applying for jobs
* Updates are idempotent

**Service Logic Reference**

```java
public void updateJobSeekerProfile(ProfileUpdateRequest request) {
    User user = getCurrentUser();
    user.completeProfile(request);
    userRepository.save(user);
}
```

---

### 🔹 Browse Jobs (Public)

**APIs**

* <span style="color:blue"><code>GET /api/jobs</code></span>
* <span style="color:blue"><code>GET /api/jobs/{id}</code></span>

**Rules**

* Only APPROVED and ACTIVE jobs are visible
* Closed or pending jobs are filtered at query level

---

### 🔹 Apply for Job

**APIs**

* <span style="color:blue"><code>POST /api/applications/{jobId}</code></span>
* <span style="color:blue"><code>GET /api/applications/my</code></span>

**Rules**

* Profile must be completed
* No duplicate applications
* Cannot apply to CLOSED jobs

**Service Logic Reference**

```java
public void apply(UUID jobId) {
    validateProfileCompletion();
    validateJobIsActive(jobId);
    preventDuplicateApplication(jobId);
    saveApplication(jobId);
}
```

---

---

## 🟣 PROVIDER (Job Poster)

---

### 🔹 Provider Profile

**API**

* <span style="color:purple"><code>PUT /api/profile/provider</code></span>

**Rules**

* Provider must be approved by admin before posting jobs

**Service Logic Reference**

```java
public void updateProviderProfile(ProviderProfileRequest request) {
    Provider provider = getCurrentProvider();
    provider.updateProfile(request);
    providerRepository.save(provider);
}
```

---

### 🔹 Job Management

**APIs**

* <span style="color:purple"><code>GET /api/provider/jobs</code></span>
* <span style="color:purple"><code>POST /api/provider/jobs</code></span>
* <span style="color:purple"><code>PUT /api/provider/jobs/{id}</code></span>
* <span style="color:purple"><code>PUT /api/provider/jobs/{id}/close</code></span>
* <span style="color:purple"><code>PUT /api/provider/jobs/{id}/reopen</code></span>

**Rules**

* Jobs are created in PENDING state
* Providers cannot approve or publish jobs
* Ownership is strictly enforced

**Service Logic Reference (Ownership Enforcement)**

```java
public Job getOwnedJob(UUID jobId) {
    Job job = getJobOrThrow(jobId);
    assertOwner(job.getProviderId());
    return job;
}
```

---

### 🔹 Application Moderation (Hiring Decisions)

**APIs**

* <code>GET /api/provider/jobs/{jobId}/applications</code>
* <code>PUT /api/provider/applications/{applicationId}/shortlist</code>
* <code>PUT /api/provider/applications/{applicationId}/reject</code>
* <code>PUT /api/provider/applications/{applicationId}/hire</code>

**Rules**

* Applications are moderated **only by the job-owning provider**
* Provider can:
  * SHORTLIST
  * REJECT
  * HIRE
* Job seekers cannot modify application status
* Admin has read-only oversight (no hiring decisions)

**Valid State Transitions**

* APPLIED → SHORTLISTED
* APPLIED → REJECTED
* SHORTLISTED → REJECTED
* SHORTLISTED → HIRED

**Service Logic Reference**

```java
public void updateApplicationStatus(UUID applicationId, ApplicationStatus status) {
    JobApplication application = getApplication(applicationId);
    assertJobOwnership(application.getJobId());
    validateTransition(application.getStatus(), status);
    application.updateStatus(status);
}
```

---

## 🔴 ADMIN (System Authority)

---

### 🔹 Job Moderation

**APIs**

* <span style="color:red"><code>GET /api/admin/jobs/pending</code></span>
* <span style="color:red"><code>PUT /api/admin/jobs/{jobId}/approve</code></span>
* <span style="color:red"><code>PUT /api/admin/jobs/{jobId}/reject</code></span>
* <span style="color:red"><code>PUT /api/admin/jobs/{jobId}/close</code></span>

**Rules**

* Only admin can change job visibility
* Approval is irreversible without admin action

**Service Logic Reference**

```java
public void approveJob(UUID jobId) {
    Job job = getJobOrThrow(jobId);
    job.approve();
    jobRepository.save(job);
}
```

---

### 🔹 Provider Approval

**APIs**

* <span style="color:red"><code>GET /api/admin/providers/pending</code></span>
* <span style="color:red"><code>PUT /api/admin/providers/{id}/approve</code></span>
* <span style="color:red"><code>PUT /api/admin/providers/{id}/reject</code></span>

**Rules**

* Unapproved providers are blocked system-wide
* Admins can view applications but cannot shortlist, reject, or hire candidates

---

## 6. Core Business Rules

* **No role escalation without admin**
* **No job visibility without approval**
* **No application without profile**
* **No provider self-governance**
* **Closed jobs are system-wide inactive**
* **Every privileged action is auditable**

---


## 7. How to Run + Why This Backend Is Strong

### ▶ How to Run

```bash
mvn clean install
mvn spring-boot:run
```

---

### 💪 Why This Backend Is Strong

* Feature-driven API design with clear ownership
* Zero role ambiguity — no implicit authority
* Explicit admin control over irreversible actions
* Clean service boundaries prevent logic leaks
* Real-world business workflows, not demo shortcuts
* Defense-in-depth security mindset

**This backend prioritizes correctness and control over convenience — it’s built to scale teams, not just traffic.**

---


# 📘 Swagger-Style APIs
---

## 🔵 USER APIs

| Method | Endpoint                                                               |
| ------ | ---------------------------------------------------------------------- |
| POST   | <span style="color:blue"><code>/api/auth/register</code></span>        |
| POST   | <span style="color:blue"><code>/api/auth/login</code></span>           |
| GET    | <span style="color:blue"><code>/api/profile/status</code></span>       |
| PUT    | <span style="color:blue"><code>/api/profile/job-seeker</code></span>   |
| GET    | <span style="color:blue"><code>/api/jobs</code></span>                 |
| GET    | <span style="color:blue"><code>/api/jobs/{id}</code></span>            |
| POST   | <span style="color:blue"><code>/api/applications/{jobId}</code></span> |
| GET    | <span style="color:blue"><code>/api/applications/my</code></span>      |

---

## 🟣 PROVIDER APIs

| Method | Endpoint                                                                      |
| ------ | ----------------------------------------------------------------------------- |
| PUT    | <span style="color:purple"><code>/api/profile/provider</code></span>          |
| GET    | <span style="color:purple"><code>/api/provider/jobs</code></span>             |
| POST   | <span style="color:purple"><code>/api/provider/jobs</code></span>             |
| PUT    | <span style="color:purple"><code>/api/provider/jobs/{id}</code></span>        |
| PUT    | <span style="color:purple"><code>/api/provider/jobs/{id}/close</code></span>  |
| PUT    | <span style="color:purple"><code>/api/provider/jobs/{id}/reopen</code></span> |
| GET    | <span style="color:purple"><code>/api/provider/dashboard/stats</code></span>  |
| GET    | <code>/api/provider/jobs/{jobId}/applications</code>                          |
| PUT    | <code>/api/provider/applications/{applicationId}/shortlist</code>             |
| PUT    | <code>/api/provider/applications/{applicationId}/reject</code>                |
| PUT    | <code>/api/provider/applications/{applicationId}/hire</code>                  |


---

## 🔴 ADMIN APIs

| Method | Endpoint                                                                         |
| ------ | -------------------------------------------------------------------------------- |
| GET    | <span style="color:red"><code>/api/admin/jobs/pending</code></span>              |
| PUT    | <span style="color:red"><code>/api/admin/jobs/{jobId}/approve</code></span>      |
| PUT    | <span style="color:red"><code>/api/admin/jobs/{jobId}/reject</code></span>       |
| PUT    | <span style="color:red"><code>/api/admin/jobs/{jobId}/close</code></span>        |
| GET    | <span style="color:red"><code>/api/admin/providers/pending</code></span>         |
| PUT    | <span style="color:red"><code>/api/admin/providers/{id}/approve</code></span>    |
| PUT    | <span style="color:red"><code>/api/admin/providers/{id}/reject</code></span>     |
| GET    | <span style="color:red"><code>/api/admin/jobs/{jobId}/applications</code></span> |
| GET    | <span style="color:red"><code>/api/admin/dashboard/stats</code></span>           |
| GET    | <span style="color:red"><code>/api/admin/dashboard/trends</code></span>          |



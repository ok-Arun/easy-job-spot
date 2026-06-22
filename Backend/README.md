# 🧠 EasyJobSpot — Spring Boot Backend

Robust, role-governed backend for a real-world job marketplace.
Designed around **strict authorization boundaries, approval workflows, and audit-safe business logic** — prioritizing correctness, control, and long-term scalability over convenience.

---

# 1. Project Overview

**EasyJobSpot Backend** enforces a controlled ecosystem where:

* Job visibility requires **admin approval**
* Privileged actions require **role + profile validation**
* Ownership is **strictly enforced at service layer**
* Administrative authority is **centralized and auditable**

Every exposed API exists to support a **clear business workflow**, not developer shortcutting.

---

# 2. Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT, stateless)**
* **Spring Data JPA / Hibernate**
* **PostgreSQL**
* **Lombok**
* **Maven**

---

# 3. Architecture Design

### Layered Responsibility Model

```
Controller → Service → Repository → Database
           ↓
          DTOs
```

### Core Principles

* Controllers contain **zero business logic**
* Services enforce **validation, ownership, and workflow rules**
* DTOs isolate **API contracts from persistence**
* Authorization enforced at **security layer + service layer**
* Admin capabilities **fully separated** from provider/user logic

Result: **no privilege leakage, no hidden authority, no logic ambiguity.**

---

# 4. Security Model

* **JWT-based stateless authentication**
* **Role-based authorization**

    * `USER`
    * `PROVIDER`
    * `ADMIN`
* **Profile completion required** before privileged actions
* **Provider approval mandatory** before job publishing
* **Admin APIs isolated** under `/api/admin/**`
* **Defense-in-depth validation** beyond annotations

---

# 5. Role Capabilities & APIs

---

## 🔵 USER — Job Seeker

### Authentication

* `POST /api/auth/register`
* `POST /api/auth/login`

**Rules**

* Registration assigns **USER role**
* JWT required for protected endpoints

---

### Profile

* `GET /api/profile/status`
* `PUT /api/profile/job-seeker`

**Rules**

* Profile completion required before **job application**
* Updates are **idempotent**

---

### Browse Jobs

* `GET /api/jobs`
* `GET /api/jobs/{id}`

**Rules**

* Only **APPROVED + ACTIVE** jobs returned
* Filtering enforced at **query level**

---

### Applications

* `POST /api/applications/{jobId}`
* `GET /api/applications/my`

**Rules**

* Profile must be complete
* Duplicate applications blocked
* Closed or inactive jobs rejected

---

## 🟣 PROVIDER — Job Poster

### Provider Profile

* `PUT /api/profile/provider`

**Rules**

* Requires **admin approval** before posting jobs

---

### Job Management

* `GET /api/provider/jobs`
* `POST /api/provider/jobs`
* `PUT /api/provider/jobs/{id}`
* `PUT /api/provider/jobs/{id}/close`
* `PUT /api/provider/jobs/{id}/reopen`

**Rules**

* New jobs start in **PENDING**
* Providers **cannot self-approve**
* Ownership strictly validated

---

### Hiring Workflow

* `GET /api/provider/jobs/{jobId}/applications`
* `PUT /api/provider/applications/{applicationId}/shortlist`
* `PUT /api/provider/applications/{applicationId}/reject`
* `PUT /api/provider/applications/{applicationId}/hire`

**Valid Transitions**

```
APPLIED → SHORTLISTED → HIRED
APPLIED → REJECTED
SHORTLISTED → REJECTED
```

Admin has **read-only oversight** (no hiring control).

---

## 🔴 ADMIN — System Authority

### Job Moderation

* `GET /api/admin/jobs/pending`
* `PUT /api/admin/jobs/{jobId}/approve`
* `PUT /api/admin/jobs/{jobId}/reject`
* `PUT /api/admin/jobs/{jobId}/close`

**Rules**

* Only admin controls **job visibility**
* Approval requires **explicit admin action**

---

### Provider Approval

* `GET /api/admin/providers/pending`
* `PUT /api/admin/providers/{id}/approve`
* `PUT /api/admin/providers/{id}/reject`

Unapproved providers are **blocked system-wide**.

---

### Platform Insights

* `GET /api/admin/jobs/{jobId}/applications`
* `GET /api/admin/dashboard/stats`
* `GET /api/admin/dashboard/trends`

---

# 6. Core Business Rules

* No **role escalation** without admin
* No **job visibility** without approval
* No **application** without completed profile
* No **provider self-governance**
* Closed jobs are **globally inactive**
* All privileged actions are **auditable**

---

# 7. Request / Response Examples

### Login

**Request**

```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "••••••••"
}
```

**Response**

```json
{
  "token": "jwt-token",
  "role": "USER"
}
```

---

### Create Job (Provider)

```json
POST /api/provider/jobs
{
  "title": "Backend Developer",
  "description": "Spring Boot experience required",
  "location": "Remote"
}
```

**Result:** Job created in **PENDING** state.

---

### Apply to Job

```json
POST /api/applications/{jobId}
```

**Checks performed**

* Profile completion
* Job active
* No duplicate application

---

# 8. Error Handling Standard

All errors follow a consistent structure:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Profile incomplete",
  "path": "/api/applications/123"
}
```

Ensures **predictable client integration** and debuggability.

---

# 9. Environment & Configuration

### Requirements

* Java **17+**
* PostgreSQL **running**
* Maven installed

### Run Locally

```bash
mvn clean install
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8081
```

---
# 9.1 Development Data Seeding

To improve developer experience and eliminate repetitive manual setup, the system includes a **data seeding mechanism** for development environments.

### Behavior

When the application starts in the `dev` profile:

- Predefined **job seekers**, **providers**, and **jobs** are automatically created
- Providers are seeded in an **APPROVED state**
- Jobs are created directly in **ACTIVE state** for immediate visibility
- Profiles are marked as **completed** to allow full workflow testing

### Purpose

This allows:

- Immediate testing of job browsing and applications
- Admin dashboard to display meaningful data
- Faster iteration without manual setup

### Important

- Seeding runs **only in development (`dev`) profile**
- Production environment strictly follows:
    - Job approval required by admin
    - Provider approval required before job posting

This ensures **no compromise in production security or workflow integrity**.
---# 9.2 Demo Accounts

EasyJobSpot ships with pre-seeded demo data to simplify testing and showcase complete business workflows.

All demo accounts use:

```text
Password: 12345678
```

---

## Administrator Account

The administrator has oversight authority over the platform.

| Role  | Email                                                 |
| ----- | ----------------------------------------------------- |
| ADMIN | [admin@easyjobspot.com](mailto:admin@easyjobspot.com) |

### Admin Capabilities

Admins can:

* Approve providers
* Reject providers
* Approve jobs
* Reject jobs
* Close jobs
* View all applications
* Access dashboard statistics
* View platform trends

Admins cannot:

* Apply to jobs
* Shortlist candidates
* Hire candidates
* Participate in hiring decisions

---

## Provider Accounts

These providers are already approved and have completed profiles.

| Company         | Email                                                                   |
| --------------- | ----------------------------------------------------------------------- |
| Google India    | [google.demo@easyjobspot.com](mailto:google.demo@easyjobspot.com)       |
| Microsoft India | [microsoft.demo@easyjobspot.com](mailto:microsoft.demo@easyjobspot.com) |
| Amazon India    | [amazon.demo@easyjobspot.com](mailto:amazon.demo@easyjobspot.com)       |

### Provider Capabilities

Providers can:

* Create jobs
* Update their own jobs
* Close and reopen their own jobs
* View applications for their jobs
* Shortlist candidates
* Reject candidates
* Hire candidates

Providers cannot:

* Approve jobs
* Approve providers
* Manage jobs owned by other providers

---

## Job Seeker Accounts

These accounts have completed profiles and are ready for testing.

| Candidate  | Email                                                           |
| ---------- | --------------------------------------------------------------- |
| Arun Demo  | [arun.demo@easyjobspot.com](mailto:arun.demo@easyjobspot.com)   |
| Rahul Demo | [rahul.demo@easyjobspot.com](mailto:rahul.demo@easyjobspot.com) |
| Priya Demo | [priya.demo@easyjobspot.com](mailto:priya.demo@easyjobspot.com) |
| Neha Demo  | [neha.demo@easyjobspot.com](mailto:neha.demo@easyjobspot.com)   |
| Amit Demo  | [amit.demo@easyjobspot.com](mailto:amit.demo@easyjobspot.com)   |

### Job Seeker Capabilities

Users can:

* Browse active jobs
* Apply to jobs
* View their own applications

Users cannot:

* Create jobs
* Access other users' applications
* Perform provider actions
* Perform admin actions

---

## Recommended Demo Walkthrough

### 1. Login as Admin

```text
Email:
admin@easyjobspot.com

Password:
12345678
```

Review:

* Pending providers
* Pending jobs
* Dashboard statistics
* Platform trends

---

### 2. Login as Provider

```text
Email:
google.demo@easyjobspot.com

Password:
12345678
```

Review:

* Provider jobs
* Applications received
* Candidate statuses

---

### 3. Login as Job Seeker

```text
Email:
arun.demo@easyjobspot.com

Password:
12345678
```

Review:

* Active jobs
* My applications
* Application statuses

---

## Seeded Application States

The demo dataset includes candidates in various stages:

```text
APPLIED
SHORTLISTED
HIRED
REJECTED
```

This allows the complete platform workflow to be demonstrated immediately without manual data creation.

---

# 10. Production Strengths[README.md](../README.md)

* Clear **domain ownership boundaries**
* Strict **approval-driven workflows**
* **Defense-in-depth** authorization
* Audit-friendly administrative control
* Clean architecture ready for **team scaling**
* Real business logic — **not demo shortcuts**

---

# 11. License

MIT License

---

# 12. Vision

EasyJobSpot is engineered as a **controlled, trustworthy hiring platform backend** capable of scaling across teams, regions, and enterprise governance requirements — while maintaining strict correctness and security guarantees.

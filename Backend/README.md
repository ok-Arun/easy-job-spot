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
http://localhost:8080
```

---

# 10. Production Strengths

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

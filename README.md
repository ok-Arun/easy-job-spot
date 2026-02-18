# 🚀 EasyJobSpot — Full Stack Hiring Platform

**EasyJobSpot** is a role-governed, approval-driven hiring platform built for **real production correctness**, not demo shortcuts.

It combines:

* ⚙️ **Spring Boot (Java 17)** backend
* 🎨 **Vanilla HTML/CSS/JS** frontend
* 🐘 **PostgreSQL** database
* 🔐 **JWT-based stateless security**

Designed around **strict authorization, auditable workflows, and scalable architecture**.

---

# 🏗️ 1. System Architecture

```
Frontend (Static UI)
        ↓ REST API
Spring Boot Backend
        ↓
PostgreSQL Database
```

### 🎯 Core Design Goals

* Clear **role separation** → USER / PROVIDER / ADMIN
* **Admin-controlled approvals & visibility**
* **Ownership enforced at service layer**
* **Defense-in-depth security model**
* Scalable, audit-safe business logic

---

# 📁 2. Repository Structure

```
easyjobspot/
│
├── backend/     → Spring Boot REST API
├── frontend/    → Static Web UI
└── README.md    → Root documentation
```

---

# 🧰 3. Tech Stack

## ⚙️ Backend

* Java 17
* Spring Boot
* Spring Security (JWT, stateless)
* Spring Data JPA / Hibernate
* PostgreSQL
* Maven

## 🎨 Frontend

* HTML5
* CSS3
* Vanilla JavaScript
* REST API integration

---

# 👥 4. User Roles & Capabilities

## 🔵 USER — Job Seeker

**Can:**

* Register & login
* Complete profile
* Browse **approved** jobs
* Apply to jobs
* Track application status

**Restrictions:**

* Cannot apply without completed profile
* Cannot view unapproved jobs

---

## 🟣 PROVIDER — Job Poster

**Can:**

* Complete provider profile
* Post jobs (**PENDING until admin approval**)
* Manage owned jobs
* Review applicants
* Shortlist / reject / hire candidates

**Restrictions:**

* Cannot self-approve jobs
* Cannot act until **admin approves provider profile**

---

## 🔴 ADMIN — System Authority

**Controls:**

* Provider approval / rejection
* Job moderation & closure
* Application visibility
* Dashboard analytics & statistics

✔ Authority is **centralized, explicit, and auditable**.

---

# 🔐 5. Security Model

* JWT-based authentication
* Role-based authorization
* Profile completion enforcement
* Provider approval workflow
* Admin-isolated endpoints → `/api/admin/**`
* Service-layer ownership validation

### Result

* ❌ No privilege leakage
* ❌ No hidden authority
* ❌ No workflow bypass
* ✅ Predictable, secure governance

---

# ▶️ 6. Running the Project Locally

## 1️⃣ Start Backend

### Requirements

* Java **17+**
* PostgreSQL **running**
* Maven **installed**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Backend URL**

```
http://localhost:8081
```

---

## 2️⃣ Start Frontend

```bash
cd frontend
python -m http.server 5500
```

**Frontend URL**

```
http://localhost:5500
```

---

## 3️⃣ Configure API Base URL

Edit:

```
frontend/js/config.js
```

```js
const API_BASE_URL = "http://localhost:8081";
```

---

# 📜 7. Core Business Rules

* Jobs visible **only after admin approval**
* Providers blocked **until approved**
* Applications require **completed profile**
* Closed jobs become **globally inactive**
* Hiring workflow enforces **valid state transitions**
* All privileged actions are **auditable**

---

# 🔌 8. API Highlights

### 🔐 Authentication

```
POST /api/auth/register
POST /api/auth/login
```

### 💼 Jobs

```
GET /api/jobs
GET /api/jobs/{id}
```

### 📄 Applications

```
POST /api/applications/{jobId}
GET /api/applications/my
```

### 🟣 Provider

```
POST /api/provider/jobs
PUT /api/provider/jobs/{id}
```

### 🔴 Admin

```
PUT /api/admin/jobs/{id}/approve
PUT /api/admin/providers/{id}/approve
GET /api/admin/dashboard/stats
```

---

# 🌍 9. Deployment

## ⚙️ Backend

* Docker (recommended)
* VPS with Java & PostgreSQL
* AWS / GCP / Azure

## 🎨 Frontend (Static Hosting)

* Netlify
* Vercel
* GitHub Pages
* Nginx

✔ Only requirement → **correct API base URL configuration**

---

# 💪 10. Production Strengths

* Strict **domain ownership boundaries**
* Approval-driven **governance workflows**
* **Defense-in-depth** authorization
* Clean **layered architecture**
* Ready for **team scaling**
* Real-world **business correctness**

---

# 🛣️ 11. Future Roadmap

* 📧 Email notifications
* 📎 Resume upload & storage
* 📊 Provider analytics dashboard
* ⚛️ React / Next.js frontend migration
* 🐳 Dockerized full-stack deployment

---

# 📄 12. License

**MIT License**

---

# 🌟 Vision

EasyJobSpot is engineered as a **controlled, trustworthy hiring platform** capable of scaling across **teams, regions, and enterprise governance requirements**—while preserving **strict correctness, security, and auditability**.

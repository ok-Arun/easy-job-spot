# 🎨 EasyJobSpot — Frontend

Role-driven **web interface** for the EasyJobSpot hiring platform, built with **vanilla HTML, CSS, and JavaScript** and tightly aligned with backend authorization, approval workflows, and real business constraints.

Designed for **clarity, strict control, and production realism** rather than framework complexity.

---

# 1. Overview

The frontend enables:

* 🔐 Secure authentication and session handling
* 🔵 Job seeker profile and application workflow
* 🟣 Provider job posting and hiring management
* 🔴 Admin moderation, approvals, and analytics
* 📊 Strict separation of **USER / PROVIDER / ADMIN** capabilities

All UI actions directly reflect **backend business rules and permissions**.

---

# 2. Tech Stack

* **HTML5**
* **CSS3 (modular role-based styling)**
* **Vanilla JavaScript (feature-segmented modules)**
* **REST API integration with Spring Boot backend**

No framework dependency → **lightweight, fast, and easy to deploy**.

---

# 3. Project Structure

```
frontend/
│
├── index.html
├── css/
├── js/
└── pages/
```

Architecture principle:

```
UI Page → JS Module → Backend API
```

---

# 4. Running the Complete System

## Step 1 — Start Backend

Backend runs on **port 8081**.

```bash
mvn spring-boot:run
```

Backend base URL:

```
http://localhost:8081
```

---

## Step 2 — Start Frontend (Local Static Server)

From the **frontend root folder**, run:

```bash
python -m http.server 5500
```

Frontend will be available at:

```
http://localhost:5500
```

---

# 5. Configuration

Update backend URL in:

```
js/config.js
```

```js
const API_BASE_URL = "http://localhost:8081";
```

---

# 6. Authentication Flow

1. User logs in → backend returns **JWT**
2. Token stored in **browser storage**
3. Protected pages validate token before rendering
4. Unauthorized users redirected to **login page**

---

# 7. User Roles in UI

## 🔵 Job Seeker

* Register & login
* Complete profile
* Browse approved jobs
* Apply to jobs
* Track application status

---

## 🟣 Provider

* Complete provider profile
* Post jobs (pending approval)
* Manage owned jobs
* Review applicants
* Shortlist / reject / hire candidates

---

## 🔴 Admin

* Approve or reject providers
* Moderate job postings
* View users and jobs
* Monitor applications
* Access dashboard statistics and trends

---

# 8. Screenshots
* 'Homepage"
  <img width="1349" height="676" alt="image" src="https://github.com/user-attachments/assets/db893c2a-1ef2-44c6-bf62-c90ddc2697de" />
  <img width="1350" height="680" alt="image" src="https://github.com/user-attachments/assets/bc7f898c-90df-4108-b166-087642774d6c" />



### Authentication

* `login.png`
  <img width="1351" height="672" alt="image" src="https://github.com/user-attachments/assets/edc25fd0-072e-4dcc-aab8-6d1bc8061157" />

* `register.png`
  <img width="1350" height="670" alt="image" src="https://github.com/user-attachments/assets/94604c7e-5721-4694-b8f8-a521191bb5c0" />


### Job Seeker

<img width="1351" height="674" alt="image" src="https://github.com/user-attachments/assets/cd2fc851-db94-43f8-94c2-5ed08403b5c5" />

* `jobs-list.png`
<img width="1364" height="563" alt="image" src="https://github.com/user-attachments/assets/2e187c91-12bb-42de-bcff-78ffb8389f6c" />

* `job-details.png`
  <img width="1337" height="663" alt="image" src="https://github.com/user-attachments/assets/397eeb7c-8c6e-4054-9d94-03b1d468f1d9" />

* `my-applications.png`
  <img width="1354" height="517" alt="image" src="https://github.com/user-attachments/assets/c7678537-0014-48be-824e-833ccdba59fc" />


### Provider
<img width="1349" height="674" alt="image" src="https://github.com/user-attachments/assets/b17efa4f-0f4e-4e50-9f7f-54e7e3f01bbc" />

* `provider-dashboard.png`
  <img width="1365" height="622" alt="image" src="https://github.com/user-attachments/assets/2be16522-40ac-4d18-a212-f44065eb9da1" />

* `post-job.png`
  <img width="1348" height="674" alt="image" src="https://github.com/user-attachments/assets/c0d5ff55-8ea3-4d3b-810c-47525c322261" />

* `job-applicants.png`
  <img width="1354" height="570" alt="image" src="https://github.com/user-attachments/assets/cc05d343-e366-4da0-b5e0-9435d34be842" />
  <img width="1363" height="552" alt="image" src="https://github.com/user-attachments/assets/2395410a-80bf-4ee9-870a-e4d61eef8c1b" />



### Admin

* `admin-dashboard.png`
  <img width="1347" height="680" alt="image" src="https://github.com/user-attachments/assets/ab635dfb-f9ea-4f95-b26c-3e3edb43c26a" />
  <img width="1344" height="680" alt="image" src="https://github.com/user-attachments/assets/c1a0f704-6c70-483f-8586-7e1ebb8efb44" />
  
* `all-users.png`
  <img width="1350" height="684" alt="image" src="https://github.com/user-attachments/assets/ca82d3b5-ae6f-4384-bd38-3244ad894e97" />

* `provider-approval.png`
  <img width="1357" height="676" alt="image" src="https://github.com/user-attachments/assets/9463f29e-1732-431e-9ff6-c135cfcb7027" />

* `all-jobs.png`
  <img width="1360" height="677" alt="image" src="https://github.com/user-attachments/assets/429c8b7b-7bb2-4d18-84b0-a919a39cf8f0" />

* `job-moderation.png`
  <img width="1354" height="673" alt="image" src="https://github.com/user-attachments/assets/ba8fa4df-ab0b-4c85-8f51-1e06fcc89f09" />


---

# 9. Deployment

Because this is a **static frontend**, deployment is simple:

* Netlify
* Vercel
* GitHub Pages
* Nginx static hosting

Only requirement: configure the **API base URL**.

---

# 10. Strengths

* Mirrors **real backend business rules**
* Clear **role-segregated UI**
* Lightweight and fast
* Easy **static deployment**
* Maintainable modular JavaScript
* Ready for **future migration to React/Next.js**

---

# 11. License

MIT License

---

# 12. Vision

This frontend serves as a **practical, extensible interface** for a controlled hiring ecosystem — ready to scale into enterprise UI systems while preserving strict authorization and workflow integrity.

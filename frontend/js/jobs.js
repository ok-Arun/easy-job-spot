document.addEventListener("DOMContentLoaded", initPage);

let appliedJobIds = new Set();

function initPage() {
    requireAuth();
    loadAppliedJobs().then(loadJobs);
}

/* ================= LOAD APPLIED JOBS ================= */

async function loadAppliedJobs() {
    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/my`,
            {
                headers: { Authorization: "Bearer " + getToken() }
            }
        );

        if (!res.ok) return;

        const response = await res.json();

        // ✅ Proper handling of paginated response
        const applications =
            response?.data?.content ||
            response?.content ||
            response?.data ||
            [];

        if (!Array.isArray(applications)) return;

        applications.forEach(app => {
            const jobId = app.jobId || app.job?.id;
            if (jobId) {
                appliedJobIds.add(String(jobId)); // force string
            }
        });

    } catch {
        // silent fail
    }
}


/* ================= READ FILTER ================= */

function getFilters() {
    const params = new URLSearchParams(window.location.search);

    return {
        title: params.get("title"),
        location: params.get("location"),
        search: params.get("search"),
        category: params.get("category")
    };
}

/* ================= LOAD JOBS ================= */

async function loadJobs() {
    const { title, location, search, category } = getFilters();

    const container = document.getElementById("jobList");
    const label = document.getElementById("activeFilter");

    try {
        let url = `${APP_CONFIG.API_BASE_URL}/jobs?`;
        const query = new URLSearchParams();

        if (title) query.append("title", title);
        if (location) query.append("location", location);
        if (!title && !location && search) query.append("search", search);
        if (!title && !location && !search && category)
            query.append("category", category);

        url += query.toString();

        const res = await fetch(url, {
            headers: { Authorization: "Bearer " + getToken() }
        });

        if (!res.ok) throw new Error();

        const data = await res.json();
        const jobs = data?.data?.content || [];

        container.innerHTML = "";

        if (jobs.length === 0) {
            container.innerHTML =
                "<p>No jobs found matching your criteria.</p>";
            return;
        }

        jobs.forEach(job => {
            const isApplied = appliedJobIds.has(job.id);

            const card = document.createElement("div");
            card.className = "job-card";
            card.style.cursor = "pointer";

            card.innerHTML = `
                <h4>${job.title}</h4>
                <div class="job-meta">${job.company} • ${job.location}</div>
                <p>${job.description.substring(0, 120)}...</p>
                <button class="apply-btn" ${isApplied ? "disabled" : ""}>
                    ${isApplied ? "Applied ✔" : "Apply"}
                </button>
            `;

            card.addEventListener("click", (e) => {
                if (!e.target.classList.contains("apply-btn")) {
                    window.location.href = `job-details.html?jobId=${job.id}`;
                }
            });

            const btn = card.querySelector(".apply-btn");

            if (!isApplied) {
                btn.addEventListener("click", async (e) => {
                    e.stopPropagation();
                    await applyToJob(job.id, btn);
                });
            }

            container.appendChild(card);
        });

    } catch {
        container.innerHTML = "<p>Unable to load jobs.</p>";
    }
}

/* ================= APPLY ================= */

async function applyToJob(jobId, button) {
    try {
        button.disabled = true;
        button.textContent = "Applying...";

        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/${jobId}`,
            {
                method: "POST",
                headers: { Authorization: "Bearer " + getToken() }
            }
        );

        const data = await res.json();

        if (!res.ok) throw new Error(data.message);

        appliedJobIds.add(jobId);
        button.textContent = "Applied ✔";

    } catch (err) {
        button.disabled = false;
        button.textContent = "Apply";
        alert(err.message || "Application failed");
    }
}

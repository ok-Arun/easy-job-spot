document.addEventListener("DOMContentLoaded", loadJobDetails);

let appliedJobIds = new Set();

/* ================= BACK ================= */

function goBack() {
    window.history.back();
}

/* ================= GET JOB ID ================= */

function getJobId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("jobId");
}

/* ================= LOAD APPLIED ================= */

async function loadAppliedJobs() {
    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/my`,
            {
                headers: { Authorization: "Bearer " + getToken() }
            }
        );

        if (!res.ok) return;

        const data = await res.json();
        const applications = data?.data || data || [];

        applications.forEach(app => {
            const jobId = app.jobId || app.job?.id;
            if (jobId) appliedJobIds.add(jobId);
        });

    } catch {}
}

/* ================= LOAD JOB ================= */

async function loadJobDetails() {
    const jobId = getJobId();
    const container = document.getElementById("jobContainer");

    if (!jobId) {
        container.innerHTML = "<p>Invalid job link.</p>";
        return;
    }

    await loadAppliedJobs();

    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/jobs/${jobId}`
        );
        if (!res.ok) throw new Error();

        const data = await res.json();
        const job = data?.data || data;

        const isApplied = appliedJobIds.has(jobId);

        container.innerHTML = `
            <h2>${job.title}</h2>
            <div class="job-meta">${job.company} • ${job.location}</div>

            <p><strong>Category:</strong> ${job.category || "N/A"}</p>
            <p><strong>Work Mode:</strong> ${job.workMode || "N/A"}</p>
            <p><strong>Job Type:</strong> ${job.jobType || "N/A"}</p>
            <p><strong>Experience:</strong> ${job.experienceMin ?? 0} - ${job.experienceMax ?? 0} yrs</p>
            <p><strong>Salary:</strong> ₹${job.salaryMin ?? 0} - ₹${job.salaryMax ?? 0}</p>

            <h3>Description</h3>
            <p>${job.description}</p>

            <button class="apply-btn" id="applyBtn" ${isApplied ? "disabled" : ""}>
                ${isApplied ? "Applied ✔" : "Apply Now"}
            </button>
        `;

        if (!isApplied) {
            document
                .getElementById("applyBtn")
                .addEventListener("click", () =>
                    apply(jobId)
                );
        }

    } catch {
        container.innerHTML = "<p>Unable to load job details.</p>";
    }
}

/* ================= APPLY ================= */

async function apply(jobId) {
    const btn = document.getElementById("applyBtn");

    try {
        btn.disabled = true;
        btn.textContent = "Applying...";

        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/${jobId}`,
            {
                method: "POST",
                headers: { Authorization: "Bearer " + getToken() }
            }
        );

        const data = await res.json();

        if (!res.ok) throw new Error(data.message);

        btn.textContent = "Applied ✔";

    } catch (err) {
        btn.disabled = false;
        btn.textContent = "Apply Now";
        alert(err.message || "Application failed");
    }
}

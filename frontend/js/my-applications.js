document.addEventListener("DOMContentLoaded", initPage);

function initPage() {
    requireAuth();
    loadApplications();
}

/* ================= LOAD APPLICATIONS ================= */

async function loadApplications() {
    const container = document.getElementById("applicationsContainer");

    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/my`,
            {
                headers: { Authorization: "Bearer " + getToken() }
            }
        );

        if (!res.ok) throw new Error();

        const response = await res.json();
        const applications = response?.data || [];

        container.innerHTML = "";

        if (applications.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <p>You have not applied to any jobs yet.</p>
                    <a href="jobs.html" class="btn-primary">Browse Jobs</a>
                </div>
            `;
            return;
        }

        applications.forEach(app => {
            const card = document.createElement("div");

            /* ✅ FIX: Added application-card modifier */
            card.className = "job-card application-card";
            card.style.cursor = "pointer";

            const statusClass = getStatusClass(app.status);

            card.innerHTML = `
                <h4>${app.jobTitle}</h4>
                <div class="job-meta">${app.company} • ${app.location}</div>
                <p><strong>Applied On:</strong> ${formatDate(app.appliedAt)}</p>
                <span class="status-badge ${statusClass}">
                    ${app.status}
                </span>
            `;

            card.addEventListener("click", () => {
                window.location.href = `job-details.html?jobId=${app.jobId}`;
            });

            container.appendChild(card);
        });

    } catch {
        container.innerHTML = "<p>Unable to load applications.</p>";
    }
}

/* ================= STATUS STYLING ================= */

function getStatusClass(status) {
    switch (status) {
        case "APPLIED":
            return "status-applied";
        case "SHORTLISTED":
            return "status-shortlisted";
        case "REJECTED":
            return "status-rejected";
        case "ACCEPTED":
            return "status-accepted";
        default:
            return "";
    }
}

/* ================= DATE FORMAT ================= */

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-IN", {
        year: "numeric",
        month: "short",
        day: "numeric"
    });
}

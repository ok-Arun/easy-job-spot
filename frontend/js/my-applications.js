document.addEventListener("DOMContentLoaded", initPage);

function initPage() {
    requireAuth();
    loadApplications();
}

/* ================= LOAD APPLICATIONS ================= */

async function loadApplications() {
    const container = document.getElementById("applicationsContainer");

    container.innerHTML = "Loading applications...";

    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/my`,
            {
                headers: {
                    Authorization: "Bearer " + getToken()
                }
            }
        );

        if (!res.ok) throw new Error("Failed to fetch applications");

        const response = await res.json();
        const applications = response?.data || [];

        container.innerHTML = "";

        /* ================= EMPTY STATE ================= */

        if (applications.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <p>You have not applied to any jobs yet.</p>
                    <a href="jobs.html" class="btn-primary">
                        Browse Jobs
                    </a>
                </div>
            `;
            return;
        }

        /* ================= RENDER CARDS ================= */

        applications.forEach(app => {

            const statusClass = app.status
                ? app.status.toLowerCase()
                : "pending";

            const card = document.createElement("div");
            card.className = "application-card";

            card.innerHTML = `
                <div class="card-header">
                    <div>
                        <h3>${app.jobTitle}</h3>
                        <p class="company">
                            ${app.company} • ${app.location}
                        </p>
                    </div>

                    <span class="status-badge ${statusClass}">
                        ${app.status}
                    </span>
                </div>

                <div class="card-footer">
                    <span class="applied-date">
                        <i class="fa-regular fa-calendar"></i>
                        Applied on ${formatDate(app.appliedAt)}
                    </span>

                    <button class="view-btn" data-id="${app.jobId}">
                        View Job
                    </button>
                </div>
            `;

            /* Prevent card click conflict */
            card.querySelector(".view-btn").addEventListener("click", (e) => {
                e.stopPropagation();
                window.location.href =
                    `job-details.html?jobId=${app.jobId}`;
            });

            container.appendChild(card);
        });

    } catch (error) {
        container.innerHTML = `
            <p style="color:red;">
                Unable to load applications. Please try again.
            </p>
        `;
    }
}

/* ================= DATE FORMAT ================= */

function formatDate(dateString) {
    if (!dateString) return "";

    const date = new Date(dateString);

    return date.toLocaleDateString("en-IN", {
        year: "numeric",
        month: "short",
        day: "numeric"
    });
}
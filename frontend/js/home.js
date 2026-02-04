// home.js

document.addEventListener("DOMContentLoaded", () => {
    handleNavbar();
    loadJobs();
});

/* ================= NAVBAR STATE ================= */

function handleNavbar() {
    const token = localStorage.getItem("token");
    const userName = localStorage.getItem("userName");

    const authActions = document.getElementById("auth-actions");
    const userActions = document.getElementById("user-actions");
    const navUserName = document.getElementById("navUserName");
    const profileIcon = document.getElementById("profileIcon");
    const profileMenu = document.getElementById("profileMenu");
    const logoutBtn = document.getElementById("logoutBtn");

    // Defensive checks (important)
    if (!authActions || !userActions) return;

    /* NOT LOGGED IN */
    if (!token) {
        authActions.classList.remove("hidden");
        userActions.classList.add("hidden");
        return;
    }

    /* LOGGED IN */
    authActions.classList.add("hidden");
    userActions.classList.remove("hidden");

    if (navUserName) {
        navUserName.textContent = userName || "User";
    }

    if (profileIcon && profileMenu) {
        profileIcon.addEventListener("click", (e) => {
            e.stopPropagation();
            profileMenu.classList.toggle("hidden");
        });

        document.addEventListener("click", () => {
            profileMenu.classList.add("hidden");
        });
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            clearToken();
            localStorage.removeItem("userName");

            // index.html is in ROOT
            window.location.href = "pages/login.html";
        });
    }
}

/* ================= JOB LIST ================= */

function loadJobs() {
    fetch(`${APP_CONFIG.API_BASE_URL}/jobs`)
        .then(res => {
            if (!res.ok) throw new Error("Failed to load jobs");
            return res.json();
        })
        .then(response => {
            const jobs = response?.data?.content || [];
            const container = document.getElementById("jobList");

            if (!container) return;

            container.innerHTML = "";

            if (jobs.length === 0) {
                container.innerHTML = "<p>No jobs available</p>";
                return;
            }

            jobs.forEach(job => {
                const div = document.createElement("div");
                div.className = "job-card";

                div.innerHTML = `
                    <h4>${job.title}</h4>
                    <div class="job-meta">
                        ${job.company} • ${job.location}
                    </div>
                    <p>${job.description.substring(0, 120)}...</p>
                `;

                container.appendChild(div);
            });
        })
        .catch(() => {
            const container = document.getElementById("jobList");
            if (container) {
                container.innerHTML = "<p>Unable to load jobs</p>";
            }
        });
}

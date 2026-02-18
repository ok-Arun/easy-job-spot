// home.js

document.addEventListener("DOMContentLoaded", () => {
    loadJobs();
    setupCategoryNavigation();
    setupSearch();
});

/* ================= SEARCH SUPPORT ================= */

function setupSearch() {
    const titleInput = document.getElementById("searchTitle");
    const locationInput = document.getElementById("searchLocation");
    const searchBtn = document.getElementById("searchBtn");

    if (!searchBtn) return;

    function performSearch() {
        const title = titleInput.value.trim();
        const location = locationInput.value.trim();

        // If nothing entered → go to all jobs
        if (!title && !location) {
            window.location.href = "pages/jobs.html";
            return;
        }

        // Build correct query params for backend
        let query = [];

        if (title) query.push(`title=${encodeURIComponent(title)}`);
        if (location) query.push(`location=${encodeURIComponent(location)}`);

        window.location.href = `pages/jobs.html?${query.join("&")}`;
    }

    searchBtn.addEventListener("click", performSearch);

    // Enter key support
    titleInput.addEventListener("keypress", e => {
        if (e.key === "Enter") performSearch();
    });

    locationInput.addEventListener("keypress", e => {
        if (e.key === "Enter") performSearch();
    });
}

/* ================= CATEGORY NAVIGATION ================= */

function setupCategoryNavigation() {
    const categoryCards = document.querySelectorAll(".category-card");

    categoryCards.forEach(card => {
        card.style.cursor = "pointer";

        card.addEventListener("click", () => {
            const category = encodeURIComponent(card.textContent.trim());
            window.location.href = `pages/jobs.html?category=${category}`;
        });
    });
}

/* ================= LOAD LATEST JOBS ================= */

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
                div.style.cursor = "pointer";

                div.innerHTML = `
                    <h4>${job.title}</h4>
                    <div class="job-meta">
                        ${job.company} • ${job.location}
                    </div>
                    <p>${job.description.substring(0, 120)}...</p>
                `;

                div.addEventListener("click", () => {
                    window.location.href = `pages/job-details.html?jobId=${job.id}`;
                });

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

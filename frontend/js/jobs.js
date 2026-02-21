document.addEventListener("DOMContentLoaded", initPage);

let appliedJobIds = new Set();
let currentPage = 0;
let totalPages = 0;
const pageSize = 6;

// ✅ Category from URL
let selectedCategoryFromUrl = null;

/* ================= INIT ================= */

function initPage() {
    requireAuth();
    readUrlParams();   // read category from URL
    setupSearch();
    setupFilters();
    loadAppliedJobs().then(() => loadJobs());
}

/* ================= READ URL PARAMS ================= */

function readUrlParams() {
    const params = new URLSearchParams(window.location.search);
    selectedCategoryFromUrl = params.get("category");
}

/* ================= SEARCH ================= */

function setupSearch() {
    const titleInput = document.getElementById("searchTitle");
    const locationInput = document.getElementById("searchLocation");
    const searchBtn = document.querySelector(".search-btn");

    function performSearch() {
        selectedCategoryFromUrl = null; // manual search overrides category
        loadJobs(0);
    }

    searchBtn.addEventListener("click", performSearch);

    titleInput.addEventListener("keypress", e => {
        if (e.key === "Enter") performSearch();
    });

    locationInput.addEventListener("keypress", e => {
        if (e.key === "Enter") performSearch();
    });
}

/* ================= FILTERS ================= */

function setupFilters() {

    const inputs = document.querySelectorAll(
        "input[name='jobType'], input[name='workMode'], input[name='employmentLevel']"
    );

    inputs.forEach(input => {
        input.addEventListener("change", () => {
            selectedCategoryFromUrl = null; // filters override category
            loadJobs(0);
        });
    });
}

/* ================= LOAD APPLIED ================= */

async function loadAppliedJobs() {
    try {
        const res = await fetch(
            `${APP_CONFIG.API_BASE_URL}/applications/my`,
            { headers: { Authorization: "Bearer " + getToken() } }
        );

        if (!res.ok) return;

        const response = await res.json();
        const applications =
            response?.data?.content ||
            response?.content ||
            response?.data ||
            [];

        if (!Array.isArray(applications)) return;

        applications.forEach(app => {
            const jobId = app.jobId || app.job?.id;
            if (jobId) appliedJobIds.add(String(jobId));
        });

    } catch {}
}

/* ================= LOAD JOBS ================= */

async function loadJobs(page = 0) {

    currentPage = page;

    const container = document.getElementById("jobList");
    const paginationContainer = document.querySelector(".pagination");

    const title = document.getElementById("searchTitle")?.value.trim();
    const location = document.getElementById("searchLocation")?.value.trim();

    const selectedJobType =
        document.querySelector("input[name='jobType']:checked")?.value;

    const selectedWorkMode =
        document.querySelector("input[name='workMode']:checked")?.value;

    const selectedEmploymentLevel =
        document.querySelector("input[name='employmentLevel']:checked")?.value;

    try {

        const query = new URLSearchParams();

        if (title) query.append("title", title);
        if (location) query.append("location", location);
        if (selectedJobType) query.append("jobType", selectedJobType);
        if (selectedWorkMode) query.append("workMode", selectedWorkMode);
        if (selectedEmploymentLevel) query.append("employmentLevel", selectedEmploymentLevel);

        // ✅ Append category from URL
        if (selectedCategoryFromUrl)
            query.append("category", selectedCategoryFromUrl);

        query.append("page", page);
        query.append("size", pageSize);

        const url = `${APP_CONFIG.API_BASE_URL}/jobs?${query.toString()}`;

        const res = await fetch(url, {
            headers: { Authorization: "Bearer " + getToken() }
        });

        if (!res.ok) throw new Error();

        const response = await res.json();
        const data = response?.data || response;

        const jobs = data?.content || [];
        totalPages = data?.totalPages || 1;

        container.innerHTML = "";

        if (jobs.length === 0) {
            container.innerHTML =
                "<p>No jobs found matching your criteria.</p>";
            return;
        }

        jobs.forEach(job => {

            const isApplied = appliedJobIds.has(String(job.id));

            const card = document.createElement("div");
            card.className = "job-card";

            card.innerHTML = `
                <h4>${job.title}</h4>
                <div class="job-meta">
                    ${job.company} • ${job.location}
                </div>
                <p>
                    ${job.description?.substring(0, 120) || ""}...
                </p>
                <div class="job-card-footer">
                    <span class="posted-time">
                        ${formatDate(job.createdAt)}
                    </span>
                    <button class="apply-btn ${isApplied ? "applied" : ""}"
                        ${isApplied ? "disabled" : ""}>
                        ${isApplied ? "Applied ✔" : "Apply Now"}
                    </button>
                </div>
            `;

            card.addEventListener("click", (e) => {
                if (!e.target.classList.contains("apply-btn")) {
                    window.location.href =
                        `job-details.html?jobId=${job.id}`;
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

        renderPagination(paginationContainer);

    } catch {
        container.innerHTML = "<p>Unable to load jobs.</p>";
    }
}

/* ================= PAGINATION ================= */

function renderPagination(container) {

    if (!container) return;

    container.innerHTML = "";

    if (totalPages <= 1) return;

    for (let i = 0; i < totalPages; i++) {

        const btn = document.createElement("button");
        btn.textContent = i + 1;

        if (i === currentPage) {
            btn.classList.add("active");
        }

        btn.addEventListener("click", () => loadJobs(i));
        container.appendChild(btn);
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

        appliedJobIds.add(String(jobId));
        button.classList.add("applied");
        button.textContent = "Applied ✔";

    } catch (err) {
        button.disabled = false;
        button.textContent = "Apply Now";
        alert(err.message || "Application failed");
    }
}

/* ================= DATE ================= */

function formatDate(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toLocaleDateString("en-IN", {
        day: "numeric",
        month: "short",
        year: "numeric"
    });
}
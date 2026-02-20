const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const JOBS_URL = `${API_BASE_URL}/provider/jobs`;

document.addEventListener("DOMContentLoaded", () => {
    requireAuth();
    loadJobs();
});


// ================= FILTER =================
function applyFilter(button, status) {
    document.querySelectorAll(".filter-btn")
        .forEach(btn => btn.classList.remove("active"));

    button.classList.add("active");
    loadJobs(status);
}


// ================= LOAD JOBS =================
async function loadJobs(status = "") {

    const state = document.getElementById("stateContainer");
    const container = document.getElementById("jobsContainer");

    showLoading(state, container);

    try {

        const url = status ? `${JOBS_URL}?status=${status}` : JOBS_URL;

        const res = await fetch(url, {
            headers: { "Authorization": "Bearer " + getToken() }
        });

        if (!res.ok) throw new Error("Failed to load jobs");

        const json = await res.json();
        const jobs = json.data?.content || [];

        state.classList.add("hidden");
        container.classList.remove("hidden");

        if (!jobs.length) {
            container.innerHTML =
                `<div class="empty-state-box">No jobs found.</div>`;
            return;
        }

        container.innerHTML = jobs.map(renderJobCard).join("");

    } catch (error) {
        console.error("Jobs error:", error);
        showError(state, container, "Failed to load jobs.");
    }
}


// ================= CARD =================
function renderJobCard(job) {

    const meta = getStatusMeta(job.status);

    return `
        <div class="job-card ${meta.cardClass}">
            <div>
                <div class="job-header">
                    <div>
                        <div class="job-title">
                            ${escapeHtml(job.title)}
                        </div>
                        <div class="job-meta">
                            ${escapeHtml(job.location)} • ${escapeHtml(job.jobType)}
                        </div>
                    </div>
                    <div class="job-status ${meta.badgeClass}">
                        ${meta.label}
                    </div>
                </div>

                <div class="job-info">
                    <div><strong>Category:</strong> ${escapeHtml(job.category)}</div>
                    <div><strong>Posted:</strong> ${formatDate(job.createdAt)}</div>
                    <div><strong>Applications:</strong> ${job.totalApplications ?? 0}</div>
                </div>
            </div>

            <div class="job-actions">
                <button class="btn btn-secondary"
                        ${job.status !== "ACTIVE" ? "disabled" : ""}
                        onclick="editJob('${job.id}')">
                    Edit
                </button>

                <button class="btn btn-secondary"
                        onclick="viewApplicants('${job.id}')">
                    View Applications
                </button>

                ${
                    job.status === "ACTIVE"
                        ? `<button class="btn btn-primary" onclick="closeJob('${job.id}')">Close Job</button>`
                        : job.status === "CLOSED"
                            ? `<button class="btn btn-primary" onclick="reopenJob('${job.id}')">Reopen Job</button>`
                            : ""
                }
            </div>
        </div>
    `;
}


// ================= STATUS META =================
function getStatusMeta(status) {
    switch (status) {
        case "ACTIVE":
            return { label: "ACTIVE", badgeClass: "badge-active", cardClass: "job-active" };
        case "PENDING_APPROVAL":
            return { label: "PENDING", badgeClass: "badge-pending", cardClass: "job-pending" };
        case "CLOSED":
        case "EXPIRED":
            return { label: "CLOSED", badgeClass: "badge-closed", cardClass: "job-closed" };
        default:
            return { label: status || "UNKNOWN", badgeClass: "", cardClass: "" };
    }
}


// ================= ACTIONS =================
async function closeJob(id) {
    openConfirmModal(
        "Close Job",
        "Are you sure you want to close this job?",
        async () => await jobAction(`${JOBS_URL}/${id}/close`)
    );
}

async function reopenJob(id) {
    openConfirmModal(
        "Reopen Job",
        "Are you sure you want to reopen this job?",
        async () => await jobAction(`${JOBS_URL}/${id}/reopen`)
    );
}

async function jobAction(url) {
    await fetch(url, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + getToken() }
    });
    loadJobs();
}


// ================= CONFIRM MODAL =================
function openConfirmModal(title, message, onConfirm) {

    const modal = document.getElementById("confirmModal");
    const okBtn = document.getElementById("confirmOk");
    const cancelBtn = document.getElementById("confirmCancel");

    document.getElementById("confirmTitle").textContent = title;
    document.getElementById("confirmMessage").textContent = message;

    modal.classList.remove("hidden");

    const close = () => modal.classList.add("hidden");

    cancelBtn.onclick = close;

    okBtn.onclick = async () => {
        okBtn.disabled = true;
        await onConfirm();
        okBtn.disabled = false;
        close();
    };
}


// ================= NAVIGATION =================
function editJob(id) {
    window.location.href = `provider-post-job.html?jobId=${id}`;
}

function viewApplicants(id) {
    window.location.href =
        `provider-job-applications.html?jobId=${id}`;
}


// ================= UI STATES =================
function showLoading(state, container) {
    state.innerHTML = `
        <div class="spinner-box">
            <div class="spinner"></div>
        </div>
    `;
    state.classList.remove("hidden");
    container.classList.add("hidden");
}

function showError(state, container, message) {
    state.innerHTML = `<div class="error-box">${message}</div>`;
    state.classList.remove("hidden");
    container.classList.add("hidden");
}


// ================= HELPERS =================
function formatDate(d) {
    return d ? new Date(d).toLocaleDateString() : "-";
}

function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text || "";
    return div.innerHTML;
}

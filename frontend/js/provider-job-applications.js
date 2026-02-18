// ================= CONFIG =================
const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const token = localStorage.getItem("token");

// ================= AUTH CHECK =================
if (!token) {
    window.location.href = "/pages/login.html";
}

const urlParams = new URLSearchParams(window.location.search);
const jobId = urlParams.get("jobId");

if (!jobId) {
    alert("Job ID missing");
    window.location.href = "/pages/provider-jobs.html";
}

// ================= INIT =================
document.addEventListener("DOMContentLoaded", () => {
    setupLogout();
    loadApplications();
});

// ================= LOAD APPLICATIONS =================
async function loadApplications() {

    const tableBody = document.getElementById("applicationsTableBody");
    tableBody.innerHTML =
        "<tr><td colspan='5'>Loading...</td></tr>";

    try {

        const response = await fetch(
            `${API_BASE_URL}/provider/jobs/${jobId}/applications`,
            {
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || "Failed to fetch applications");
        }

        const applications = result.data || [];
        tableBody.innerHTML = "";

        if (!applications.length) {
            tableBody.innerHTML =
                "<tr><td colspan='5'>No applications found</td></tr>";
            return;
        }

        applications.forEach(app => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${escapeHtml(app.applicantName)}</td>
                <td>${escapeHtml(app.applicantEmail)}</td>
                <td>${formatDate(app.appliedAt)}</td>
                <td>${renderStatusBadge(app.status)}</td>
                <td>${renderActionButtons(app)}</td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Applications error:", error);
        tableBody.innerHTML =
            "<tr><td colspan='5'>Error loading applications</td></tr>";
    }
}

// ================= STATUS BADGE =================
function renderStatusBadge(status) {

    switch (status) {
        case "APPLIED":
            return `<span class="application-status status-applied">APPLIED</span>`;
        case "SHORTLISTED":
            return `<span class="application-status status-shortlisted">SHORTLISTED</span>`;
        case "REJECTED":
            return `<span class="application-status status-rejected">REJECTED</span>`;
        case "HIRED":
            return `<span class="application-status status-hired">HIRED</span>`;
        default:
            return status;
    }
}

// ================= ACTION BUTTONS =================
function renderActionButtons(app) {

    const status = app.status;

    if (status === "HIRED" || status === "REJECTED") {
        return `<span>-</span>`;
    }

    if (status === "SHORTLISTED") {
        return `
            <div class="application-actions">
                <button class="btn btn-primary btn-sm"
                    onclick="hire('${app.id}')">
                    Hire
                </button>
                <button class="btn btn-danger btn-sm"
                    onclick="rejectApp('${app.id}')">
                    Reject
                </button>
            </div>
        `;
    }

    return `
        <div class="application-actions">
            <button class="btn btn-secondary btn-sm"
                onclick="shortlist('${app.id}')">
                Shortlist
            </button>
            <button class="btn btn-danger btn-sm"
                onclick="rejectApp('${app.id}')">
                Reject
            </button>
        </div>
    `;
}

// ================= ACTIONS =================
async function shortlist(applicationId) {
    await updateStatus(applicationId, "shortlist");
}

async function rejectApp(applicationId) {
    await updateStatus(applicationId, "reject");
}

async function hire(applicationId) {
    await updateStatus(applicationId, "hire");
}

async function updateStatus(applicationId, action) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/provider/applications/${applicationId}/${action}`,
            {
                method: "PUT",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        // DO NOT blindly parse JSON
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Failed to update status");
        }

        // Refresh list after successful update
        await loadApplications();

    } catch (error) {
        console.error("Status update error:", error);
        alert("Action failed");
    }
}

// ================= LOGOUT =================
function setupLogout() {
    const logoutBtn = document.getElementById("logoutBtn");
    if (!logoutBtn) return;

    logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("token");
        window.location.href = "/pages/login.html";
    });
}

// ================= HELPERS =================
function formatDate(dateStr) {
    return dateStr ? new Date(dateStr).toLocaleString() : "-";
}

function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text || "";
    return div.innerHTML;
}

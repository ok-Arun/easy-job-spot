const API_BASE = "http://localhost:8081/api/admin/jobs";
const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {

    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    applyURLFilter();
    fetchAllJobs();

    document.getElementById("filterBtn").addEventListener("click", () => {
        fetchAllJobs();
    });
});


// ================= READ STATUS FROM URL =================

function applyURLFilter() {
    const params = new URLSearchParams(window.location.search);
    const statusFromURL = params.get("status");

    const dropdown = document.getElementById("statusFilter");

    if (statusFromURL && dropdown) {
        dropdown.value = statusFromURL;
    }
}


// ================= FETCH WITH FILTER =================

async function fetchAllJobs() {

    const status = document.getElementById("statusFilter").value;

    let url = API_BASE;

    if (status && status.trim() !== "") {
        url += `?status=${status}`;
    }

    try {
        const response = await fetch(url, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const result = await response.json();

        if (!result.success) {
            alert("Failed to load jobs");
            return;
        }

        renderJobs(result.data.content);

    } catch (error) {
        console.error(error);
        alert("Error loading jobs");
    }
}


// ================= RENDER =================

function renderJobs(jobs) {

    const tableBody = document.getElementById("allJobsTableBody");
    tableBody.innerHTML = "";

    if (!jobs || jobs.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center; padding:20px;">
                    No jobs found.
                </td>
            </tr>
        `;
        return;
    }

    jobs.forEach(job => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td data-label="Title">${job.title}</td>
            <td data-label="Company">${job.company}</td>
            <td data-label="Location">${job.location}</td>
            <td data-label="Status">
                <span class="status-badge ${job.status}">
                    ${job.status}
                </span>
            </td>
            <td data-label="Posted On">
                ${formatDate(job.createdAt)}
            </td>
            <td data-label="Actions">
                ${renderActions(job)}
            </td>
        `;

        tableBody.appendChild(row);
    });
}


// ================= ACTION BUTTONS =================

function renderActions(job) {

    const viewBtn = `
        <button class="view-btn"
                onclick="viewApplications('${job.id}')">
            Applications
        </button>
    `;

    if (job.status === "REMOVED_BY_ADMIN") {
        return `
            ${viewBtn}
            <button class="restore-btn"
                    onclick="openModal('restore','${job.id}')">
                Restore
            </button>
        `;
    }

    if (job.status === "CLOSED" || job.status === "REJECTED" || job.status === "EXPIRED") {
        return `
            ${viewBtn}
            <button class="remove-btn"
                    onclick="openModal('remove','${job.id}')">
                Remove
            </button>
        `;
    }

    return `
        ${viewBtn}
        <button class="close-btn"
                onclick="openModal('close','${job.id}')">
            Close
        </button>
        <button class="remove-btn"
                onclick="openModal('remove','${job.id}')">
            Remove
        </button>
    `;
}


// ================= VIEW APPLICATIONS =================

function viewApplications(jobId) {
    window.location.href = `admin-job-applications.html?jobId=${jobId}`;
}


// ================= UTIL =================

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

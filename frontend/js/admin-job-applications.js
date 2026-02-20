const API_BASE = "https://easy-job-spot-production.up.railway.app/api/admin/jobs";
const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {

    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    const jobId = getJobIdFromURL();

    if (!jobId) {
        alert("Invalid Job ID");
        return;
    }

    fetchApplications(jobId);
});


// ================= GET JOB ID FROM URL =================

function getJobIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("jobId");
}


// ================= FETCH APPLICATIONS =================

async function fetchApplications(jobId) {

    try {
        const response = await fetch(`${API_BASE}/${jobId}/applications`, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const result = await response.json();

        if (!result.success) {
            alert("Failed to load applications");
            return;
        }

        renderApplications(result.data);

    } catch (error) {
        console.error(error);
        alert("Error loading applications");
    }
}


// ================= RENDER =================

function renderApplications(applications) {

    const tableBody = document.getElementById("applicationsTableBody");
    tableBody.innerHTML = "";

    if (!applications || applications.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center; padding:20px;">
                    No applications found.
                </td>
            </tr>
        `;
        return;
    }

    applications.forEach(app => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td data-label="Name">${app.applicantName}</td>
            <td data-label="Email">${app.applicantEmail}</td>
            <td data-label="Status">
                <span class="status-badge ${app.status}">
                    ${app.status}
                </span>
            </td>
            <td data-label="Applied At">
                ${formatDate(app.appliedAt)}
            </td>
            <td data-label="Rejection Reason">
                ${app.rejectionReason ? app.rejectionReason : "-"}
            </td>
        `;

        tableBody.appendChild(row);
    });
}


// ================= UTIL =================

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

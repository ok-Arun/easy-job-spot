const BASE_URL = "https://easy-job-spot-production.up.railway.app/api/admin/jobs";

let selectedJobId = null;
let selectedJobData = null;
let actionType = null;

document.addEventListener("DOMContentLoaded", () => {

    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    fetchPendingJobs();
});


/* ================= FETCH ================= */

async function fetchPendingJobs() {

    try {
        const response = await fetch(`${BASE_URL}/pending`, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        });

        const result = await response.json();

        if (!result.success) return;

        // ✅ set pending count correctly
        document.getElementById("totalPendingJobs").innerText =
            result.data.totalPending || 0;

        renderJobs(result.data.content);

    } catch (error) {
        console.error("Error fetching jobs:", error);
    }
}


/* ================= RENDER ================= */

function renderJobs(jobs) {

    const tableBody = document.getElementById("jobsTableBody");
    tableBody.innerHTML = "";

    if (!jobs || jobs.length === 0) {
        tableBody.innerHTML =
            `<tr><td colspan="6" style="text-align:center;">No pending jobs</td></tr>`;
        return;
    }

    jobs.forEach(job => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${job.title}</td>
            <td>${job.company}</td>
            <td>${job.location}</td>
            <td>${formatDate(job.createdAt)}</td>
            <td>${getStatusBadge(job.status)}</td>
            <td>
                <button class="view-btn">View</button>
                <button class="approve-btn">Approve</button>
                <button class="reject-btn">Reject</button>
            </td>
        `;

        row.querySelector(".view-btn").addEventListener("click", () => openViewModal(job));
        row.querySelector(".approve-btn").addEventListener("click", () => openConfirmModal("approve", job));
        row.querySelector(".reject-btn").addEventListener("click", () => openConfirmModal("reject", job));

        tableBody.appendChild(row);
    });
}


/* ================= VIEW ================= */

function openViewModal(job) {

    selectedJobData = job;

    const details = document.getElementById("jobDetails");

    details.innerHTML = `
        <p><strong>Title:</strong> ${job.title}</p>
        <p><strong>Company:</strong> ${job.company}</p>
        <p><strong>Location:</strong> ${job.location}</p>
        <p><strong>Status:</strong> ${job.status}</p>
        <p><strong>Work Mode:</strong> ${job.workMode || "-"}</p>
        <p><strong>Employment Level:</strong> ${job.employmentLevel || "-"}</p>
        <p><strong>Salary:</strong> ${job.salaryMin || "-"} - ${job.salaryMax || "-"}</p>
        <p><strong>Experience:</strong> ${job.experienceMin || "-"} - ${job.experienceMax || "-"}</p>
        <p><strong>Vacancies:</strong> ${job.vacancyCount || "-"}</p>
        <hr>
        <p><strong>Description:</strong></p>
        <p>${job.description}</p>
    `;

    document.getElementById("viewModal").classList.remove("hidden");
}

function closeViewModal() {
    document.getElementById("viewModal").classList.add("hidden");
}


/* ================= CONFIRM ================= */

function openConfirmModal(type, job) {

    selectedJobId = job.id;
    actionType = type;

    const title = document.getElementById("confirmTitle");
    const message = document.getElementById("confirmMessage");
    const reasonBox = document.getElementById("rejectReason");
    const actionBtn = document.getElementById("confirmActionBtn");

    if (type === "approve") {

        title.innerText = "Approve Job";
        message.innerText = `Approve "${job.title}"?`;
        reasonBox.classList.add("hidden");
        actionBtn.innerText = "Approve";
        actionBtn.className = "approve-btn";

    } else {

        title.innerText = "Reject Job";
        message.innerText = `Reject "${job.title}"?`;
        reasonBox.classList.remove("hidden");
        reasonBox.value = "";
        actionBtn.innerText = "Reject";
        actionBtn.className = "reject-btn";
    }

    actionBtn.onclick = confirmAction;

    document.getElementById("confirmModal").classList.remove("hidden");
}

function closeConfirmModal() {
    document.getElementById("confirmModal").classList.add("hidden");
}


/* ================= ACTION ================= */

async function confirmAction() {

    if (!selectedJobId) return;

    const headers = {
        "Authorization": "Bearer " + localStorage.getItem("token")
    };

    try {

        if (actionType === "approve") {

            await fetch(`${BASE_URL}/${selectedJobId}/approve`, {
                method: "PUT",
                headers
            });

        } else {

            const reason = document.getElementById("rejectReason").value.trim();

            if (!reason) {
                document.getElementById("rejectReason").style.border = "1px solid red";
                return;
            }

            await fetch(
                `${BASE_URL}/${selectedJobId}/reject?reason=${encodeURIComponent(reason)}`,
                {
                    method: "PUT",
                    headers
                }
            );
        }

        closeConfirmModal();
        fetchPendingJobs();

    } catch (error) {
        console.error("Action failed", error);
    }
}


/* ================= HELPERS ================= */

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

function getStatusBadge(status) {

    let className = "badge";

    if (status === "PENDING_APPROVAL") className += " badge-pending";
    else if (status === "ACTIVE") className += " badge-active";
    else if (status === "REJECTED") className += " badge-rejected";

    return `<span class="${className}">${status}</span>`;
}

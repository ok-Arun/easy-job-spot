const BASE_URL = "http://localhost:8081/api/admin/providers";

let selectedProviderId = null;
let actionType = null;

document.addEventListener("DOMContentLoaded", () => {

    closeConfirmModal();   // Always start hidden

    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    fetchPendingProviders();
});

/* ================= FETCH ================= */

async function fetchPendingProviders() {

    try {
        const response = await fetch(`${BASE_URL}/pending`, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        });

        const result = await response.json();
        const data = result.data || result;

        document.getElementById("totalPending").innerText =
            data.totalPending || 0;

        renderProviders(data.providers || []);

    } catch (error) {
        console.error("Error fetching providers:", error);
    }
}

/* ================= RENDER ================= */

function renderProviders(providers) {

    const tbody = document.getElementById("providersTableBody");
    tbody.innerHTML = "";

    if (!providers.length) {
        tbody.innerHTML =
            `<tr><td colspan="4" style="text-align:center;">No pending providers</td></tr>`;
        return;
    }

    providers.forEach(provider => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${provider.name}</td>
            <td>${provider.email}</td>
            <td><span class="badge badge-pending">${provider.providerStatus}</span></td>
            <td>
                <button class="approve-btn">Approve</button>
                <button class="reject-btn">Reject</button>
            </td>
        `;

        row.querySelector(".approve-btn")
            .addEventListener("click", () => openConfirmModal("approve", provider));

        row.querySelector(".reject-btn")
            .addEventListener("click", () => openConfirmModal("reject", provider));

        tbody.appendChild(row);
    });
}

/* ================= MODAL CONTROL ================= */

function openConfirmModal(type, provider) {

    selectedProviderId = provider.id;
    actionType = type;

    const modal = document.getElementById("confirmModal");
    const title = document.getElementById("confirmTitle");
    const message = document.getElementById("confirmMessage");
    const actionBtn = document.getElementById("confirmActionBtn");

    if (type === "approve") {
        title.innerText = "Approve Provider";
        message.innerText = `Approve "${provider.name}"?`;
        actionBtn.innerText = "Approve";
        actionBtn.className = "approve-btn";
    } else {
        title.innerText = "Reject Provider";
        message.innerText = `Reject "${provider.name}"?`;
        actionBtn.innerText = "Reject";
        actionBtn.className = "reject-btn";
    }

    actionBtn.onclick = confirmAction;

    modal.classList.add("show");
}

function closeConfirmModal() {
    document.getElementById("confirmModal")
        .classList.remove("show");
}

/* ================= ACTION ================= */

async function confirmAction() {

    if (!selectedProviderId) return;

    const endpoint =
        actionType === "approve"
            ? `${BASE_URL}/${selectedProviderId}/approve`
            : `${BASE_URL}/${selectedProviderId}/reject`;

    try {
        await fetch(endpoint, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        });

        closeConfirmModal();
        fetchPendingProviders();

    } catch (error) {
        console.error("Action failed:", error);
    }
}

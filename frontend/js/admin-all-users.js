const API = "http://localhost:8081/api/admin/users";
const token = localStorage.getItem("token");

if (!token) location.href = "/login.html";

let currentPage = 0;
const pageSize = 10;

/* ================= INIT ================= */

document.addEventListener("DOMContentLoaded", () => {
    applyURLFilter();
    loadUsers(0);
});

/* ================= READ FILTER FROM URL ================= */

function applyURLFilter() {
    const params = new URLSearchParams(window.location.search);
    const userTypeFromURL = params.get("userType");

    if (userTypeFromURL) {
        const dropdown = document.getElementById("roleFilter");
        dropdown.value = userTypeFromURL;
    }
}

/* ================= LOAD USERS ================= */

async function loadUsers(page = 0) {
    try {
        currentPage = page;

        const search = document.getElementById("searchInput").value.trim();
        const userType = document.getElementById("roleFilter").value;

        let url = `${API}?page=${currentPage}&size=${pageSize}`;

        if (search) url += `&search=${encodeURIComponent(search)}`;
        if (userType) url += `&userType=${encodeURIComponent(userType)}`;

        const res = await fetch(url, {
            headers: { Authorization: "Bearer " + token }
        });

        const result = await res.json();

        if (!result.success) {
            alert("Failed to load users");
            return;
        }

        const data = result.data;
        const pageData = data.users;
        const users = pageData.content || [];

        // ===== stats =====
        document.getElementById("totalUsers").innerText = data.totalUsers;
        document.getElementById("seekers").innerText = data.seekers;
        document.getElementById("providers").innerText = data.providers;
        document.getElementById("admins").innerText = data.admins;

        renderTable(users);
        renderPagination(pageData);

    } catch (err) {
        console.error(err);
        alert("Error loading users");
    }
}

/* ================= TABLE ================= */

function renderTable(users) {
    const tbody = document.getElementById("usersTable");

    if (!users.length) {
        tbody.innerHTML = `<tr><td colspan="5" class="empty-row">No users found</td></tr>`;
        return;
    }

    tbody.innerHTML = users.map(u => `
        <tr>
            <td data-label="Name">${u.name}</td>
            <td data-label="Email">${u.email}</td>
            <td data-label="Role">
                <span class="role-badge ${u.userType}">
                    ${u.userType.replace("_", " ")}
                </span>
            </td>
            <td data-label="Status">
                <span class="status-active">Active</span>
            </td>
            <td data-label="Actions">
                <button class="delete-btn" onclick="deleteUser('${u.id}')">
                    Delete
                </button>
            </td>
        </tr>
    `).join("");
}

/* ================= PAGINATION ================= */

function renderPagination(pageData) {
    let container = document.getElementById("pagination");
    if (!container) return;

    const totalPages = pageData.totalPages;
    const pageNumber = pageData.number;

    let buttons = "";

    for (let i = 0; i < totalPages; i++) {
        buttons += `
            <button
                class="page-btn ${i === pageNumber ? "active" : ""}"
                onclick="loadUsers(${i})">
                ${i + 1}
            </button>
        `;
    }

    container.innerHTML = buttons;
}

/* ================= DELETE ================= */

async function deleteUser(id) {
    if (!confirm("Delete user?")) return;

    await fetch(`${API}/${id}`, {
        method: "DELETE",
        headers: { Authorization: "Bearer " + token }
    });

    loadUsers(currentPage);
}

/* ================= FILTER UX ================= */

document.getElementById("searchInput")
    .addEventListener("keypress", e => {
        if (e.key === "Enter") loadUsers(0);
    });

document.getElementById("roleFilter")
    .addEventListener("change", () => loadUsers(0));

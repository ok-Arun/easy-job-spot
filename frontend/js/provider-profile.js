const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const message = document.getElementById("message");

// ================= TOKEN GUARD =================
const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "login.html";
}

// ================= MESSAGE =================
function showMessage(text, type) {
    message.innerText = text;
    message.className = `form-message ${type}`;
    message.style.display = "block";
}

// ================= FETCH PROFILE =================
document.addEventListener("DOMContentLoaded", () => {
    fetchProfile();
});

async function fetchProfile() {
    try {
        const res = await fetch(`${API_BASE_URL}/profile/me`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) {
            showMessage("Failed to load profile.", "error");
            return;
        }

        const profile = await res.json();

        // If empty object → first time profile
        if (!profile || Object.keys(profile).length === 0) {
            document.getElementById("approvalStatus").innerText = "PENDING";
            return;
        }

        document.getElementById("companyName").value = profile.companyName || "";
        document.getElementById("companyEmail").value = profile.companyEmail || "";
        document.getElementById("companyPhone").value = profile.companyPhone || "";
        document.getElementById("address").value = profile.address || "";
        document.getElementById("description").value = profile.description || "";
        document.getElementById("website").value = profile.website || "";

        // Approval badge
        const badge = document.getElementById("approvalStatus");
        const status = profile.approvedAt ? "APPROVED" : "PENDING";

        badge.innerText = status;
        badge.classList.remove("approved", "pending", "rejected");

        if (status === "APPROVED") {
            badge.classList.add("approved");
        } else {
            badge.classList.add("pending");
        }

    } catch (err) {
        showMessage("Server error while loading profile.", "error");
    }
}

// ================= UPDATE PROFILE =================
document.getElementById("providerForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
        companyName: document.getElementById("companyName").value.trim(),
        companyEmail: document.getElementById("companyEmail").value.trim(),
        companyPhone: document.getElementById("companyPhone").value.trim(),
        address: document.getElementById("address").value.trim(),
        description: document.getElementById("description").value.trim(),
        website: document.getElementById("website").value.trim()
    };

    for (const key in payload) {
        if (!payload[key]) {
            showMessage("All fields are mandatory.", "error");
            return;
        }
    }

    try {
        const res = await fetch(`${API_BASE_URL}/profile/me`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        const data = await res.json();

        if (!res.ok || !data.success) {
            showMessage(data.message || "Profile update failed.", "error");
            return;
        }

        showMessage("Profile updated successfully.", "success");

        setTimeout(() => {
            window.location.href = "/provider-dashboard.html";
        }, 1000);

    } catch (err) {
        showMessage("Server error.", "error");
    }
});

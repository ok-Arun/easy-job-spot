const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const token = localStorage.getItem("token");
const message = document.getElementById("message");

// ================= TOKEN GUARD =================
if (!token) {
    window.location.href = "login.html";
}

// ================= MESSAGE HANDLER =================
function showMessage(text, type) {
    message.innerText = text;
    message.className = `form-message ${type}`;
    message.style.display = "block";
}

// ================= FETCH PROFILE ON LOAD =================
document.addEventListener("DOMContentLoaded", () => {
    fetchProfile();
});

async function fetchProfile() {
    try {
        const res = await fetch(`${API_BASE_URL}/profile/provider`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        const data = await res.json();

        if (!res.ok || !data.success) {
            showMessage(data.message || "Failed to load profile.", "error");
            return;
        }

        const profile = data.data;

        document.getElementById("companyName").value = profile.companyName || "";
        document.getElementById("companyEmail").value = profile.companyEmail || "";
        document.getElementById("companyPhone").value = profile.companyPhone || "";
        document.getElementById("address").value = profile.address || "";
        document.getElementById("description").value = profile.description || "";
        document.getElementById("website").value = profile.website || "";

        // Approval Status Badge
        const badge = document.getElementById("approvalStatus");
        const status = profile.approvalStatus || "PENDING";

        badge.innerText = status;

        badge.classList.remove("approved", "pending", "rejected");

        if (status.toUpperCase() === "APPROVED") {
            badge.classList.add("approved");
        } else if (status.toUpperCase() === "REJECTED") {
            badge.classList.add("rejected");
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

    // Final frontend validation
    for (const key in payload) {
        if (!payload[key]) {
            showMessage("All fields are mandatory. Please complete your company profile.", "error");
            return;
        }
    }

    try {
        const res = await fetch(`${API_BASE_URL}/profile/provider`, {
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

        showMessage("Company profile updated successfully. Redirecting to dashboard…", "success");

        localStorage.setItem("profileCompleted", "1");

        setTimeout(() => {
            window.location.href = "/provider-dashboard.html";
        }, 1200);

    } catch (err) {
        showMessage("Server error. Please try again.", "error");
    }
});

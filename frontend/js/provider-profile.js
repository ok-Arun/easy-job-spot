const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const token = localStorage.getItem("token");
const message = document.getElementById("message");

// Hard guard (same as job seeker)
if (!token) {
    window.location.href = "login.html";
}

function showMessage(text, type) {
    message.innerText = text;
    message.className = `message-bar ${type}`;
    message.style.display = "block";
}

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

    // Final frontend validation (same pattern as seeker)
    for (const key in payload) {
        if (!payload[key]) {
            showMessage("All fields are mandatory. Please complete your company profile.", "error");
            return;
        }
    }

    try {
        const res = await fetch(`${API_BASE_URL}/profile/provider`, {
            method: "PUT", // must match backend
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

        // SUCCESS
        showMessage("Company profile updated successfully. Redirecting to dashboard…", "success");

        // same UX optimization
        localStorage.setItem("profileCompleted", "1");

        setTimeout(() => {
            window.location.href = "/index.html";
        }, 1200);

    } catch (err) {
        showMessage("Server error. Please try again.", "error");
    }
});

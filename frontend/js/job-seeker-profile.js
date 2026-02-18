const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const token = localStorage.getItem("token");
const message = document.getElementById("message");

// Hard guard (optional but recommended)
if (!token) {
    window.location.href = "login.html";
}

function showMessage(text, type) {
    message.innerText = text;
    message.className = `message-bar ${type}`;
    message.style.display = "block";
}

document.getElementById("jobSeekerForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
        firstName: document.getElementById("firstName").value.trim(),
        lastName: document.getElementById("lastName").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        location: document.getElementById("location").value.trim(),
        skills: document.getElementById("skills").value.trim(),
        education: document.getElementById("education").value.trim(),
        experience: document.getElementById("experience").value.trim(),
        currentJobTitle: document.getElementById("currentJobTitle").value.trim(),
        preferredJobType: document.getElementById("preferredJobType").value.trim(),
        preferredLocation: document.getElementById("preferredLocation").value.trim(),
        noticePeriod: document.getElementById("noticePeriod").value.trim(),
        resumeUrl: document.getElementById("resumeUrl").value.trim(),
        linkedinUrl: document.getElementById("linkedinUrl").value.trim(),
        portfolioUrl: document.getElementById("portfolioUrl").value.trim()
    };

    // Final frontend validation (matches Postman)
    for (const key in payload) {
        if (!payload[key]) {
            showMessage("All fields are mandatory. Please complete your profile.", "error");
            return;
        }
    }

    try {
        const res = await fetch(`${API_BASE_URL}/profile/job-seeker`, {
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

        // SUCCESS
        showMessage("Profile updated successfully. Redirecting to dashboard…", "success");

        // Update local flag (optional UX optimization)
        localStorage.setItem("profileCompleted", "1");

        setTimeout(() => {
            window.location.href = "/index.html";
        }, 1200);

    } catch (err) {
        showMessage("Server error. Please try again.", "error");
    }
});

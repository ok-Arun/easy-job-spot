const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const message = document.getElementById("message");

const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "login.html";
}

function showMessage(text, type) {
    message.innerText = text;
    message.className = `form-message ${type}`;
    message.style.display = "block";
}

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

        if (!profile || Object.keys(profile).length === 0) {
            return; // first-time user
        }

        document.getElementById("firstName").value = profile.firstName || "";
        document.getElementById("lastName").value = profile.lastName || "";
        document.getElementById("phone").value = profile.phone || "";
        document.getElementById("location").value = profile.location || "";
        document.getElementById("skills").value = profile.skills || "";
        document.getElementById("education").value = profile.education || "";
        document.getElementById("experience").value = profile.experience || "";
        document.getElementById("currentJobTitle").value = profile.currentJobTitle || "";
        document.getElementById("preferredJobType").value = profile.preferredJobType || "";
        document.getElementById("preferredLocation").value = profile.preferredLocation || "";
        document.getElementById("noticePeriod").value = profile.noticePeriod || "";
        document.getElementById("resumeUrl").value = profile.resumeUrl || "";
        document.getElementById("linkedinUrl").value = profile.linkedinUrl || "";
        document.getElementById("portfolioUrl").value = profile.portfolioUrl || "";

    } catch (err) {
        showMessage("Server error while loading profile.", "error");
    }
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
            window.location.href = "/index.html";
        }, 1000);

    } catch (err) {
        showMessage("Server error.", "error");
    }
});

// login.js

const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;

async function login() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    hideMessage();

    if (!email || !password) {
        showMessage("Email and password are required", "error");
        return;
    }

    try {
        // 1️⃣ LOGIN
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (!response.ok) {
            showMessage(data.message || "Invalid email or password", "error");
            return;
        }

        // 2️⃣ SAVE AUTH DATA
        saveToken(data.token);
        localStorage.setItem("userName", data.name);
        localStorage.setItem("userType", data.userType);

        // 3️⃣ PROVIDER APPROVAL CHECK
        if (data.userType === "JOB_PROVIDER") {
            if (data.providerStatus === "PENDING") {
                clearToken();
                showMessage("Your account approval is pending.", "error");
                return;
            }
            if (data.providerStatus === "REJECTED") {
                clearToken();
                showMessage("Your account has been rejected.", "error");
                return;
            }
        }

        // 4️⃣ PROFILE STATUS CHECK
        await checkProfileStatus();

    } catch (err) {
        console.error(err);
        showMessage("Server error. Please try again later.", "error");
    }
}

/* =========================
   PROFILE STATUS (TOKEN)
   ========================= */

async function checkProfileStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/profile/status`, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + getToken()
            }
        });

        if (!response.ok) {
            showMessage("Unable to verify profile status.", "error");
            return;
        }

        const data = await response.json();
        const isProfileComplete = data.profileCompleted;
        const userType = data.userType;

        // ✅ PROFILE COMPLETE → HOME
        if (isProfileComplete === true) {
            showSuccessAndRedirect(
                "Login successful! Redirecting to homepage...",
                2000
            );
            return;
        }

        // ❌ PROFILE INCOMPLETE → PROFILE PAGE
        showMessage("Please complete your profile.", "error");

        setTimeout(() => {
            window.location.href =
                userType === "JOB_SEEKER"
                    ? "/pages/job-seeker-profile.html"
                    : "/pages/provider-profile.html";
        }, 3000);

    } catch (error) {
        console.error("Profile status error:", error);
        showMessage("Unable to check profile status.", "error");
    }
}


/* =========================
   UI HELPERS
   ========================= */

function showMessage(message, type = "error") {
    const box = document.getElementById("messageBox");
    box.innerText = message;
    box.classList.remove("hidden");
    box.classList.remove("error", "success");
    box.classList.add(type);
}

function hideMessage() {
    document.getElementById("messageBox").classList.add("hidden");
}

function showSuccessAndRedirect(message, delay) {
    showMessage(message, "success");
    setTimeout(() => {
        window.location.href = "/index.html";
    }, delay);
}

/* =========================
   PASSWORD TOGGLE
   ========================= */

function togglePassword() {
    const input = document.getElementById("password");
    const icon = document.getElementById("eyeIcon");

    if (input.type === "password") {
        input.type = "text";
        icon.classList.replace("fa-eye", "fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.replace("fa-eye-slash", "fa-eye");
    }
}

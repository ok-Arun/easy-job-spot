// register.js

const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;

function register() {
    const nameEl = document.getElementById("name");
    const emailEl = document.getElementById("email");
    const passwordEl = document.getElementById("password");
    const userTypeEl = document.getElementById("userType");

    hideMessage();

    const name = nameEl.value.trim();
    const email = emailEl.value.trim();
    const password = passwordEl.value;
    const userType = userTypeEl.value;

    // ===== BASIC VALIDATION =====
    if (!userType) {
        showMessage("Please select Job Seeker or Job Provider", "error");
        return;
    }

    if (!name) {
        showMessage("Full name is required", "error");
        return;
    }

    if (!email) {
        showMessage("Email address is required", "error");
        return;
    }

    if (!password) {
        showMessage("Password is required", "error");
        return;
    }

    if (password.length < 6) {
        showMessage("Password must be at least 6 characters", "error");
        return;
    }

    // ===== REQUEST BODY =====
    const body = {
        name: name,
        email: email,
        password: password,
        userType: userType
    };

    // ===== API CALL =====
    fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
    .then(async response => {
        const data = await response.json();

        if (!response.ok) {
            showMessage(data.message || "Registration failed", "error");
            return;
        }

        showMessage(
            "Account created successfully. Redirecting to login...",
            "success"
        );

        setTimeout(() => {
            window.location.href = "./login.html";
        }, 1500);
    })
    .catch(err => {
        console.error("Register error:", err);
        showMessage("Server error. Please try again later.", "error");
    });
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

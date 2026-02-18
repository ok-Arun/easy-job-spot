// ================= CONFIG =================
const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;


// ================= LOGIN =================
async function login() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    hideMessage();

    if (!email || !password) {
        showMessage("Email and password are required", "error");
        return;
    }

    try {
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

        // ===== PROVIDER APPROVAL CHECK =====
        if (data.userType === "JOB_PROVIDER") {
            if (data.providerStatus === "PENDING") {
                clearAuthSession();
                showMessage("Your account approval is pending.", "error");
                return;
            }

            if (data.providerStatus === "REJECTED") {
                clearAuthSession();
                showMessage("Your account has been rejected.", "error");
                return;
            }
        }

        // ===== SAVE TEMP SESSION (profile status unknown yet) =====
        saveAuthSession({
            token: data.token,
            userType: data.userType,
            userName: data.name,
            profileCompleted: false
        });

        // ===== VERIFY PROFILE STATUS =====
        await checkProfileStatus();

    } catch (err) {
        console.error("Login error:", err);
        showMessage("Server error. Please try again later.", "error");
    }
}


// ================= PROFILE STATUS =================
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

        const isProfileComplete = data.profileCompleted === true;
        const userType = data.userType;

        // ===== UPDATE SESSION WITH REAL PROFILE STATUS =====
        saveAuthSession({
            token: getToken(),
            userType: userType,
            userName: getUserName(),
            profileCompleted: isProfileComplete
        });

        // ===== PROFILE COMPLETE → REDIRECT =====
        if (isProfileComplete) {
            showMessage("Login successful", "success");

            setTimeout(() => redirectAfterLogin(userType), 500);
            return;
        }

        // ===== PROFILE INCOMPLETE → PROFILE PAGE =====
        showMessage("Please complete your profile.", "error");

        setTimeout(() => {
            window.location.href =
                userType === "JOB_SEEKER"
                    ? "/pages/job-seeker-profile.html"
                    : "/pages/provider-profile.html";
        }, 1500);

    } catch (error) {
        console.error("Profile status error:", error);
        showMessage("Unable to check profile status.", "error");
    }
}


// ================= REDIRECT LOGIC =================
function redirectAfterLogin(userType) {
    const routes = {
        SYSTEM_ADMIN: "/pages/admin-dashboard.html",
        JOB_PROVIDER: "/pages/provider-dashboard.html",
        JOB_SEEKER: "/index.html"
    };

    if (!routes[userType]) {
        console.error("Unknown user type:", userType);
        clearAuthSession();
        window.location.href = "/pages/login.html";
        return;
    }

    window.location.href = routes[userType];
}


// ================= UI HELPERS =================
function showMessage(message, type = "error") {
    const box = document.getElementById("messageBox");
    box.innerText = message;
    box.classList.remove("hidden", "error", "success");
    box.classList.add(type);
}

function hideMessage() {
    document.getElementById("messageBox").classList.add("hidden");
}


// ================= PASSWORD TOGGLE =================
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

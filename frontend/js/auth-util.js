// auth-util.js

const TOKEN_KEY = "token";
const USER_TYPE_KEY = "userType";
const USER_NAME_KEY = "userName";
const PROFILE_COMPLETED_KEY = "profileCompleted";

/* =========================
   SAVE AUTH SESSION
   ========================= */

function saveAuthSession({ token, userType, userName, profileCompleted }) {
    if (!token) {
        console.error("No token provided to saveAuthSession()");
        return;
    }

    localStorage.setItem(TOKEN_KEY, token);

    if (userType) localStorage.setItem(USER_TYPE_KEY, userType);
    if (userName) localStorage.setItem(USER_NAME_KEY, userName);
    if (profileCompleted !== undefined) {
        localStorage.setItem(PROFILE_COMPLETED_KEY, profileCompleted ? "1" : "0");
    }
}

/* =========================
   GETTERS
   ========================= */

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function getUserType() {
    return localStorage.getItem(USER_TYPE_KEY);
}

function getUserName() {
    return localStorage.getItem(USER_NAME_KEY);
}

function isProfileCompleted() {
    return localStorage.getItem(PROFILE_COMPLETED_KEY) === "1";
}

/* =========================
   CLEAR SESSION
   ========================= */

function clearAuthSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_TYPE_KEY);
    localStorage.removeItem(USER_NAME_KEY);
    localStorage.removeItem(PROFILE_COMPLETED_KEY);
}

/* =========================
   ROUTE GUARDS
   ========================= */

// Redirect to login if not authenticated
function requireAuth() {
    if (!getToken()) {
        window.location.href = "/pages/login.html";
    }
}

// Redirect to correct profile page if incomplete
function requireCompletedProfile() {
    if (!isProfileCompleted()) {
        const type = getUserType();

        window.location.href =
            type === "JOB_SEEKER"
                ? "/pages/job-seeker-profile.html"
                : "/pages/provider-profile.html";
    }
}

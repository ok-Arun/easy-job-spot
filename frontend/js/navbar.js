// navbar.js

document.addEventListener("DOMContentLoaded", () => {
    const token = getToken();
    const rawUserName = getUserName();

    const authActions = document.getElementById("auth-actions");
    const userActions = document.getElementById("user-actions");
    const userNameEl = document.getElementById("navUserName");
    const logoutBtn = document.getElementById("logoutBtn");
    const editProfileBtn = document.getElementById("editProfileBtn");
    const ctaSection = document.getElementById("ctaSection");

    const logo = document.querySelector(".logo");
    const profileIcon = document.getElementById("profileIcon");
    const profileMenu = document.getElementById("profileMenu");

    /* ================= LOGO REDIRECT ================= */
    if (logo) {
        logo.addEventListener("click", () => {
            window.location.href = "/index.html";
        });
    }

    /* ================= NOT LOGGED IN ================= */
    if (!token) {
        if (authActions) authActions.classList.remove("hidden");
        if (userActions) userActions.classList.add("hidden");
        if (ctaSection) ctaSection.style.display = "block";
        return;
    }

    /* ================= LOGGED IN ================= */
    if (authActions) authActions.classList.add("hidden");
    if (userActions) userActions.classList.remove("hidden");

    if (userNameEl) {
        userNameEl.innerText = formatName(rawUserName) || "User";
    }

    if (ctaSection) ctaSection.style.display = "none";

    /* ================= DROPDOWN TOGGLE ================= */
    if (profileIcon && profileMenu) {
        profileIcon.addEventListener("click", (e) => {
            e.stopPropagation();
            profileMenu.classList.toggle("hidden");
        });

        document.addEventListener("click", () => {
            profileMenu.classList.add("hidden");
        });
    }

    /* ================= LOGOUT ================= */
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

    /* ================= EDIT PROFILE ================= */
    if (editProfileBtn) editProfileBtn.addEventListener("click", editProfile);
});


/* ================= NAME FORMATTER ================= */

function formatName(name) {
    if (!name) return "";

    return name
        .trim()
        .toLowerCase()
        .split(" ")
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");
}


/* ================= LOGOUT FUNCTION ================= */

function logout() {
    clearAuthSession();
    window.location.replace("/pages/login.html");
}


/* ================= ROLE-BASED PROFILE REDIRECT ================= */

function editProfile() {
    const userType = getUserType();

    if (!userType) {
        window.location.replace("/pages/login.html");
        return;
    }

    switch (userType) {
        case "JOB_SEEKER":
            window.location.href = "/pages/job-seeker-profile.html";
            break;

        case "JOB_PROVIDER":
            window.location.href = "/pages/provider-profile.html";
            break;

        case "SYSTEM_ADMIN":
            window.location.href = "/pages/admin-dashboard.html";
            break;

        default:
            logout();
    }
}

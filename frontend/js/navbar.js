// navbar.js

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    const userName = localStorage.getItem("userName");

    const authButtons = document.getElementById("authButtons");
    const userMenu = document.getElementById("userMenu");
    const userNameEl = document.getElementById("userName");

    // Not logged in
    if (!token) {
        if (authButtons) authButtons.style.display = "flex";
        if (userMenu) userMenu.style.display = "none";
        return;
    }

    // Logged in
    if (authButtons) authButtons.style.display = "none";
    if (userMenu) userMenu.style.display = "flex";
    if (userNameEl) userNameEl.innerText = userName || "User";
});

function logout() {
    // Clear only what we use
    localStorage.removeItem("token");
    localStorage.removeItem("userName");
    localStorage.removeItem("userType");

    // Redirect to login page (based on your structure)
    window.location.href = "pages/login.html";
}

function editProfile() {
    const userType = localStorage.getItem("userType");

    if (!userType) {
        window.location.href = "pages/login.html";
        return;
    }

    window.location.href =
        userType === "JOB_SEEKER"
            ? "pages/job-seeker-profile.html"
            : "pages/provider-profile.html";
}

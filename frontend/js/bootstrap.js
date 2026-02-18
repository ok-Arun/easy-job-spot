// bootstrap.js
// Runs on every protected page

document.addEventListener("DOMContentLoaded", () => {
    enforceAuthentication();
    enforceProfileCompletion();
    hydrateNavbarUser();
});


/* =========================
   AUTH GUARD
   ========================= */

function enforceAuthentication() {
    const token = getToken();

    if (!token) {
        window.location.href = "/pages/login.html";
    }
}


/* =========================
   PROFILE COMPLETION GUARD
   ========================= */

function enforceProfileCompletion() {
    const completed = isProfileCompleted();
    const userType = getUserType();

    if (!completed) {
        window.location.href =
            userType === "JOB_SEEKER"
                ? "/pages/job-seeker-profile.html"
                : "/pages/provider-profile.html";
    }
}


/* =========================
   NAVBAR USER HYDRATION
   ========================= */

function hydrateNavbarUser() {
    const nameEl = document.getElementById("userName");
    const userName = getUserName();

    if (nameEl && userName) {
        nameEl.innerText = userName;
    }
}

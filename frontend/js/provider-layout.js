document.addEventListener("DOMContentLoaded", async () => {

    try {
        // Load navbar
        const response = await fetch("provider-navbar.html");
        const navbarHTML = await response.text();

        // Insert navbar at top of body
        document.body.insertAdjacentHTML("afterbegin", navbarHTML);

        setActiveNav();
        setupLogout();

    } catch (error) {
        console.error("Failed to load navbar:", error);
    }
});


// ================= ACTIVE NAV =================
function setActiveNav() {

    const links = document.querySelectorAll(".nav-links a");
    const currentPage = window.location.pathname.split("/").pop();

    links.forEach(link => {

        const href = link.getAttribute("href");

        if (href === currentPage) {
            link.parentElement.classList.add("active");
        } else {
            link.parentElement.classList.remove("active");
        }

    });
}


// ================= LOGOUT =================
function setupLogout() {

    const logoutBtn = document.getElementById("logoutBtn");

    if (!logoutBtn) return;

    logoutBtn.addEventListener("click", () => {

        localStorage.removeItem("token");
        window.location.href = "login.html";

    });
}

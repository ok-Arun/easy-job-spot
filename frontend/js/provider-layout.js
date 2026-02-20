document.addEventListener("DOMContentLoaded", async () => {

    try {
        const response = await fetch("provider-navbar.html");
        const navbarHTML = await response.text();

        document.body.insertAdjacentHTML("afterbegin", navbarHTML);

        setActiveNav();
        setupLogout();
        setupMobileMenu();

    } catch (error) {
        console.error("Failed to load navbar:", error);
    }
});


// ================= ACTIVE NAV =================
function setActiveNav() {

    const links = document.querySelectorAll(".nav-links a");
    const currentPage = window.location.pathname.split("/").pop();
    const navPageTitle = document.getElementById("navPageTitle");

    links.forEach(link => {

        const href = link.getAttribute("href");

        if (href === currentPage) {
            link.parentElement.classList.add("active");

            // Show current page below logo
            if (navPageTitle) {
                navPageTitle.textContent = link.textContent;
            }

        } else {
            link.parentElement.classList.remove("active");
        }
    });
}


// ================= MOBILE MENU =================
function setupMobileMenu() {

    const menuToggle = document.getElementById("menuToggle");
    const navLinks = document.getElementById("navLinks");

    if (!menuToggle || !navLinks) return;

    menuToggle.addEventListener("click", () => {
        navLinks.classList.toggle("active");
    });

    navLinks.querySelectorAll("a, button").forEach(item => {
        item.addEventListener("click", () => {
            navLinks.classList.remove("active");
        });
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

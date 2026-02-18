// ===== AUTH GUARD =====
(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/pages/login.html";
        return;
    }

    // ===== INSERT LOGOUT BUTTON =====
    document.addEventListener("DOMContentLoaded", () => {

        const header =
            document.querySelector(".dashboard-header") ||
            document.querySelector(".page-header");

        if (!header) return;

        if (header.querySelector(".logout-btn")) return;

        const btn = document.createElement("button");
        btn.innerText = "Logout";
        btn.className = "logout-btn";

        btn.onclick = () => {
            localStorage.removeItem("token");
            window.location.href = "/pages/login.html";
        };

        header.appendChild(btn);
    });

})();

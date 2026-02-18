// ===== AUTH GUARD =====
(function () {

    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/pages/login.html";
        return;
    }

// ===== FETCH WRAPPER WITH AUTO LOGOUT =====
async function secureFetch(url, options = {}) {
    const token = localStorage.getItem("token");

    const res = await fetch(url, {
        ...options,
        headers: {
            ...(options.headers || {}),
            Authorization: "Bearer " + token
        }
    });

    // token expired or invalid
    if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/pages/login.html";
        return;
    }

    return res;
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

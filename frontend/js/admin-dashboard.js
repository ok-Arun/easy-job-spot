// ================= CONFIG =================
const BASE_URL = window.APP_CONFIG.API_BASE_URL + "/admin/dashboard";

let jobsChart = null;
let applicationsChart = null;
let hiringChart = null;


// ================= INIT =================
document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");

    // 🔥 FIX — use replace to avoid cached broken page
    if (!token) {
        window.location.replace("/pages/login.html");
        return;
    }

    highlightSidebar();
    fetchStats(token);
    fetchTrends(token);
});


// ================= SIDEBAR =================
function highlightSidebar() {
    const links = document.querySelectorAll(".sidebar a");
    const currentPage = window.location.pathname.split("/").pop().toLowerCase();

    links.forEach(link => {
        link.classList.remove("active");

        const linkPage = link.getAttribute("href").toLowerCase();
        if (linkPage === currentPage) {
            link.classList.add("active");
        }
    });
}


// ================= FETCH STATS =================
async function fetchStats(token) {
    try {
        const response = await fetch(`${BASE_URL}/stats`, {
            headers: { "Authorization": "Bearer " + token }
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            console.error("Stats API failed", result);
            return;
        }

        const data = result.data;

        // JOBS
        document.getElementById("totalJobs").innerText = data.jobs.total;
        document.getElementById("pendingJobs").innerText = data.jobs.pending;
        document.getElementById("activeJobs").innerText = data.jobs.active;

        // APPLICATIONS
        document.getElementById("totalApplications").innerText = data.applications.total;
        document.getElementById("shortlisted").innerText = data.applications.shortlisted;
        document.getElementById("rejected").innerText = data.applications.rejected;
        document.getElementById("hired").innerText = data.applications.hired;

        // USERS
        if (data.users) {
            document.getElementById("totalUsers").innerText = data.users.total;
            document.getElementById("jobSeekers").innerText = data.users.jobSeekers;
            document.getElementById("providers").innerText = data.users.providers;
            document.getElementById("admins").innerText = data.users.admins;
            document.getElementById("pendingProviders").innerText = data.users.pendingProviders;
        }

    } catch (error) {
        console.error("Failed to fetch stats:", error);
    }
}


// ================= FETCH TRENDS =================
async function fetchTrends(token) {
    try {
        const response = await fetch(`${BASE_URL}/trends`, {
            headers: { "Authorization": "Bearer " + token }
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            console.error("Trends API failed", result);
            return;
        }

        const data = result.data;

        renderJobsTrend(data.jobs);
        renderApplicationsTrend(data.applications);
        renderHiringFunnel(data.hiringFunnel);

    } catch (error) {
        console.error("Failed to fetch trends:", error);
    }
}


// ================= CHARTS =================
function renderJobsTrend(jobs) {
    if (jobsChart) jobsChart.destroy();

    jobsChart = new Chart(document.getElementById("jobsTrendChart"), {
        type: "bar",
        data: {
            labels: ["Created (30d)", "Approved (30d)"],
            datasets: [{
                label: "Jobs",
                data: [jobs.created.last30Days, jobs.approved.last30Days],
                backgroundColor: ["#3b82f6", "#10b981"],
                borderRadius: 6,
                barPercentage: 0.5
            }]
        },
        options: baseBarOptions()
    });
}

function renderApplicationsTrend(applications) {
    if (applicationsChart) applicationsChart.destroy();

    applicationsChart = new Chart(document.getElementById("applicationsTrendChart"), {
        type: "bar",
        data: {
            labels: ["Applications (30d)"],
            datasets: [{
                label: "Applications",
                data: [applications.received.last30Days],
                backgroundColor: "#6366f1",
                borderRadius: 6,
                barPercentage: 0.4
            }]
        },
        options: baseBarOptions()
    });
}

function renderHiringFunnel(funnel) {
    if (hiringChart) hiringChart.destroy();

    hiringChart = new Chart(document.getElementById("hiringFunnelChart"), {
        type: "doughnut",
        data: {
            labels: ["Applied", "Shortlisted", "Rejected", "Hired"],
            datasets: [{
                data: [funnel.applied, funnel.shortlisted, funnel.rejected, funnel.hired],
                backgroundColor: ["#2563eb", "#14b8a6", "#dc2626", "#16a34a"],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: "65%",
            plugins: {
                legend: { position: "top", labels: { color: "#e2e8f0" } }
            }
        }
    });
}

function baseBarOptions() {
    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { labels: { color: "#e2e8f0" } }
        },
        scales: {
            x: { ticks: { color: "#94a3b8" }, grid: { display: false } },
            y: {
                ticks: { color: "#94a3b8" },
                grid: { color: "rgba(148,163,184,0.1)" },
                beginAtZero: true
            }
        }
    };
}

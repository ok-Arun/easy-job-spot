const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const POST_JOB_URL = `${API_BASE_URL}/jobs`;

document.addEventListener("DOMContentLoaded", () => {
    requireAuth();
    setupLogout();
    populateDropdowns();          // ⭐ added
    setupApplicationTypeToggle();
    setupForm();
});

/* ================= DROPDOWNS ================= */

function populateDropdowns() {

    setOptions("category", [
        "Software Development",
        "IT Support / Helpdesk",
        "Data Entry / Back Office",
        "BPO / Telecaller / Voice",
        "Finance & Accounting",
        "Digital Marketing",
        "Content Writing",
        "Customer Support",
        "Sales & Business Development",
        "HR & Recruitment",
        "Graphic Design",
        "Virtual Assistant",
        "Other"
    ]);

    setOptions("workMode", [
        "Remote",
        "Hybrid",
        "On-site"
    ]);

    setOptions("jobType", [
        "Full-time",
        "Part-time",
        "Contract",
        "Internship",
        "Freelance"
    ]);

    setOptions("employmentLevel", [
        "Fresher",
        "Entry Level",
        "Mid Level",
        "Senior Level",
        "Lead / Manager"
    ]);

    setOptions("applicationType", [
        "INTERNAL",
        "EXTERNAL"
    ]);
}

function setOptions(selectId, options) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = `<option value="">Select</option>`;

    options.forEach(opt => {
        const option = document.createElement("option");
        option.value = opt;
        option.textContent = opt;
        select.appendChild(option);
    });
}

/* ================= FORM SUBMIT ================= */

function setupForm() {
    const form = document.getElementById("postJobForm");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            title: getValue("title"),
            company: getUserName(),
            category: getValue("category"),
            location: getValue("location"),
            jobType: getValue("jobType"),
            description: getValue("description"),

            workMode: getValue("workMode"),
            employmentLevel: getValue("employmentLevel"),

            salaryMin: Number(getValue("salaryMin")),
            salaryMax: Number(getValue("salaryMax")),

            experienceMin: Number(getValue("expMin")),
            experienceMax: Number(getValue("expMax")),

            vacancyCount: Number(getValue("vacancyCount")),

            applicationType: getValue("applicationType"),
            applicationUrl:
                getValue("applicationType") === "EXTERNAL"
                    ? getOptionalValue("applicationUrl")
                    : null,

            deadline: getValue("deadline") + "T23:59:59"
        };

        try {
            const response = await fetch(POST_JOB_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + getToken()
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Failed to post job");
            }

            showMessage("Job posted successfully!", "success");

            setTimeout(() => {
                window.location.href = "/pages/provider-jobs.html";
            }, 1200);

        } catch (err) {
            console.error(err);
            showMessage(err.message, "error");
        }
    });
}

/* ================= APPLICATION TYPE TOGGLE ================= */

function setupApplicationTypeToggle() {
    const typeSelect = document.getElementById("applicationType");
    const urlGroup = document.getElementById("externalUrlGroup");

    typeSelect.addEventListener("change", () => {
        if (typeSelect.value === "EXTERNAL") {
            urlGroup.style.display = "block";
        } else {
            urlGroup.style.display = "none";
            document.getElementById("applicationUrl").value = "";
        }
    });
}

/* ================= LOGOUT ================= */

function setupLogout() {
    document.getElementById("logoutBtn")
        .addEventListener("click", () => {
            clearAuthSession();
            window.location.href = "/pages/login.html";
        });
}

/* ================= HELPERS ================= */

function getValue(id) {
    return document.getElementById(id).value.trim();
}

function getOptionalValue(id) {
    const value = getValue(id);
    return value === "" ? null : value;
}

function showMessage(msg, type) {
    const el = document.getElementById("formMessage");
    el.textContent = msg;
    el.className = `form-message ${type}`;
}

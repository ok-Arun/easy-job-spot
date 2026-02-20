const API_BASE_URL = window.APP_CONFIG.API_BASE_URL;
const POST_JOB_URL = `${API_BASE_URL}/jobs`;

document.addEventListener("DOMContentLoaded", () => {
    requireAuth();

    populateDropdowns();

    // 🔥 Force hide on load
    const urlGroup = document.getElementById("externalUrlGroup");
    if (urlGroup) {
        urlGroup.classList.add("hidden");
    }

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

    setOptions("workMode", ["Remote", "Hybrid", "On-site"]);

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

    setOptions("applicationType", ["INTERNAL", "EXTERNAL"]);
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


/* ================= APPLICATION TYPE TOGGLE ================= */

function setupApplicationTypeToggle() {

    const typeSelect = document.getElementById("applicationType");
    const urlGroup = document.getElementById("externalUrlGroup");

    if (!typeSelect || !urlGroup) return;

    typeSelect.addEventListener("change", () => {

        if (typeSelect.value === "EXTERNAL") {
            urlGroup.classList.remove("hidden");
        } else {
            urlGroup.classList.add("hidden");
            document.getElementById("applicationUrl").value = "";
        }

    });
}


/* ================= FORM SUBMIT ================= */

function setupForm() {

    const form = document.getElementById("postJobForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const applicationType = getValue("applicationType");

        const payload = {
            title: getValue("title"),
            category: getValue("category"),
            location: getValue("location"),
            jobType: getValue("jobType"),
            description: getValue("description"),
            workMode: getValue("workMode"),
            employmentLevel: getValue("employmentLevel"),
            salaryMin: Number(getValue("salaryMin")) || null,
            salaryMax: Number(getValue("salaryMax")) || null,
            experienceMin: Number(getValue("expMin")) || null,
            experienceMax: Number(getValue("expMax")) || null,
            vacancyCount: Number(getValue("vacancyCount")) || null,
            applicationType: applicationType,
            applicationUrl:
                applicationType === "EXTERNAL"
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
                window.location.href = "provider-jobs.html";
            }, 1200);

        } catch (err) {
            console.error(err);
            showMessage(err.message || "Something went wrong", "error");
        }

    });
}


/* ================= HELPERS ================= */

function getValue(id) {
    const el = document.getElementById(id);
    return el ? el.value.trim() : "";
}

function getOptionalValue(id) {
    const value = getValue(id);
    return value === "" ? null : value;
}

function showMessage(msg, type) {
    const el = document.getElementById("formMessage");
    if (!el) return;

    el.textContent = msg;
    el.className = `form-message ${type}`;
    el.classList.remove("hidden");
}

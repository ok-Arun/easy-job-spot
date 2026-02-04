// Example: role stored after login
// localStorage.setItem("role", "JOB_SEEKER");
// localStorage.setItem("token", "JWT_TOKEN_HERE");

const role = localStorage.getItem("role");
const token = localStorage.getItem("token");

const jobSeekerForm = document.getElementById("jobSeekerForm");
const providerForm = document.getElementById("providerForm");
const message = document.getElementById("message");

// Show correct form
if (role === "JOB_SEEKER") {
    jobSeekerForm.classList.remove("hidden");
} else if (role === "PROVIDER") {
    providerForm.classList.remove("hidden");
} else {
    message.innerText = "Invalid user role";
}

// Job Seeker Submit
jobSeekerForm?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
        fullName: jsFullName.value,
        skills: jsSkills.value,
        experience: jsExperience.value,
        location: jsLocation.value
    };

    await submitProfile("/api/profile/job-seeker", payload);
});

// Provider Submit
providerForm?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
        companyName: companyName.value,
        companyType: companyType.value,
        location: companyLocation.value,
        website: website.value
    };

    await submitProfile("/api/profile/provider", payload);
});

async function submitProfile(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (!response.ok) {
            message.style.color = "red";
            message.innerText = result.message || "Error saving profile";
            return;
        }

        message.style.color = "green";
        message.innerText = "Profile saved successfully";

    } catch (error) {
        message.style.color = "red";
        message.innerText = "Server error";
    }
}

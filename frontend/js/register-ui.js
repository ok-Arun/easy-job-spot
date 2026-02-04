function selectRole(role) {
    const body = document.body;

    document.getElementById("jobSeekerCard").classList.remove("active");
    document.getElementById("providerCard").classList.remove("active");

    if (role === "JOB_SEEKER") {
        document.getElementById("jobSeekerCard").classList.add("active");
        body.classList.remove("provider-theme");
        body.classList.add("jobseeker-theme");
    }

    if (role === "JOB_PROVIDER") {
        document.getElementById("providerCard").classList.add("active");
        body.classList.remove("jobseeker-theme");
        body.classList.add("provider-theme");
    }

    document.getElementById("userType").value = role;
}

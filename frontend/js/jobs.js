document.addEventListener("DOMContentLoaded", loadJobs);

function loadJobs() {
    fetch(`${API_BASE_URL}/jobs`, {
        headers: getAuthHeaders()
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("Unauthorized");
        }
        return res.json();
    })
    .then(jobs => {
        const container = document.getElementById("jobList");
        container.innerHTML = "";

        if (jobs.length === 0) {
            container.innerHTML = "<p>No jobs available</p>";
            return;
        }

        jobs.forEach(job => {
            const div = document.createElement("div");
            div.style.border = "1px solid #ccc";
            div.style.padding = "10px";
            div.style.marginBottom = "10px";

            div.innerHTML = `
                <h4>${job.title}</h4>
                <p>${job.description}</p>
                <p><b>Location:</b> ${job.location}</p>
                <button onclick="applyJob(${job.id})">Apply</button>
            `;

            container.appendChild(div);
        });
    })
    .catch(() => {
        alert("Please login first");
        window.location.href = "login.html";
    });
}

function applyJob(jobId) {
    fetch(`${API_BASE_URL}/applications/${jobId}`, {
        method: "POST",
        headers: getAuthHeaders()
    })
    .then(res => res.json())
    .then(data => alert(data.message));
}

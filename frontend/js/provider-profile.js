const token = localStorage.getItem("token");
const message = document.getElementById("message");

document.getElementById("providerForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const payload = {
        companyName: companyName.value,
        companyEmail: companyEmail.value,
        companyPhone: companyPhone.value,
        address: address.value,
        description: description.value,
        website: website.value || null
    };

    try {
        const res = await fetch("/api/profile/provider", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        const data = await res.json();

        if (!res.ok) {
            message.style.color = "red";
            message.innerText = data.message || "Profile save failed";
            return;
        }

        message.style.color = "green";
        message.innerText = "Profile saved successfully";

    } catch {
        message.style.color = "red";
        message.innerText = "Server error";
    }
});

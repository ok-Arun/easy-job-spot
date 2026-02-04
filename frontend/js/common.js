function getAuthHeaders() {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

function handleUnauthorized(response) {
    if (response.status === 401 || response.status === 403) {
        alert("Session expired or access denied");
        logout();
        return true;
    }
    return false;
}

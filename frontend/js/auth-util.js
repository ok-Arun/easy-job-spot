// auth-util.js

const TOKEN_KEY = "token"; // unified key

function saveToken(token) {
    if (!token) {
        console.error("No token provided to saveToken()");
        return;
    }
    localStorage.setItem(TOKEN_KEY, token);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function clearToken() {
    localStorage.removeItem(TOKEN_KEY);
}

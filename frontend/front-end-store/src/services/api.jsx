import AuthService from "./AuthService";
const API_BASE_URL = import.meta.env.VITE_BACKEND_URL;

export async function apiFetch(url, options = {}, noAuth = false) {
    let token = !noAuth ? AuthService.getAccessToken() : null;

    const headers = {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
    };

    let response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
        credentials: "include",
    });

    if (!noAuth && response.status === 401) {
        const refreshed = await AuthService.refreshToken();
        if (!refreshed) throw new Error("Unauthorized");

        token = AuthService.getAccessToken();
        response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers: {
                ...headers,
                Authorization: `Bearer ${token}`,
            },
            credentials: "include",
        });
    }

    return response;
}

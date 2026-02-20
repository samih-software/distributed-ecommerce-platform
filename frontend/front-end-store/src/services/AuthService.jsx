const API_BASE_URL = import.meta.env.VITE_BACKEND_URL;

let accessToken = null;

class AuthService {
    static getAccessToken() {
        return accessToken;
    }

    static setAccessToken(token) {
        accessToken = token;
    }

    static async login(email, password) {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include", // required for refresh cookie
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            let message = "Login failed";
            try {
                const data = await response.json();
                if (data.error) message = data.error;
            } catch {}
            return { success: false, message };
        }

        const data = await response.json();
        this.setAccessToken(data.token);
        return { success: true };
    }

    static async refreshToken() {
        const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
            method: "POST",
            credentials: "include",
        });

        if (!response.ok) {
            this.setAccessToken(null);
            return false;
        }

        const data = await response.json();
        this.setAccessToken(data.token);
        return true;
    }

    static logout() {
        this.setAccessToken(null);
    }
}

export default AuthService;
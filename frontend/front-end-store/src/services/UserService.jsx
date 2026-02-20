import { apiFetch } from "./api";

export const UserService = {
    getProfile: async () => {
        const response = await apiFetch("/auth/me");
        if (!response.ok) throw new Error("Failed to fetch user");
        return response.json();
    },

    updateProfile: async (userId, { name, email }) => {
        const response = await apiFetch(`/users/${userId}`, {
            method: "PUT",
            body: JSON.stringify({ name, email }),
            headers: { "Content-Type": "application/json" },
        });
        if (!response.ok) throw new Error("Failed to update profile");
    },

    changePassword: async (userId, oldPassword, newPassword) => {
        const response = await apiFetch(`/users/${userId}/change-password`, {
            method: "POST",
            body: JSON.stringify({ oldPassword, newPassword }),
            headers: { "Content-Type": "application/json" },
        });
        if (!response.ok) throw new Error("Failed to change password");
    },
};

import { apiFetch } from "./api";

export const OrderService = {
    getProducts: async () => {
        const response = await apiFetch("/products");
        if (!response.ok) throw new Error("Failed to load products");
        return response.json();
    },

    getCart: async (cartId) => {
        const response = await apiFetch(`/carts/${cartId}`);
        if (!response.ok) throw new Error("Failed to load cart");
        return response.json();
    },

    createCart: async () => {
        const response = await apiFetch("/carts", { method: "POST" });
        if (!response.ok) throw new Error("Failed to create cart");
        return response.json();
    },

    addItem: async (cartId, productId) => {
        await apiFetch(`/carts/${cartId}/items`, {
            method: "POST",
            body: JSON.stringify({ productId }),
        });
    },

    removeItem: async (cartId, productId) => {
        await apiFetch(`/carts/${cartId}/items/${productId}`, {
            method: "DELETE",
        });
    },

    clearCart: async (cartId) => {
        await apiFetch(`/carts/${cartId}/items`, { method: "DELETE" });
    },

    getOrders: async () => {
        const response = await apiFetch("/orders");
        if (!response.ok) throw new Error("Failed to load orders");
        return response.json();
    },

    getDelivery: async (orderId) => {
        const response = await apiFetch(`/deliveries/${orderId}`);
        if (!response.ok) throw new Error("Failed to load delivery info");
        return response.json();
    },

    checkout: async (cartId) => {
        const response = await apiFetch("/checkout", {
            method: "POST",
            body: JSON.stringify({ cartId }),
        });
        if (!response.ok) throw new Error("Checkout failed");
        return response.json();
    },

    createDelivery: async (deliveryInfo) => {
        const response = await apiFetch("/deliveries", {
            method: "POST",
            body: JSON.stringify(deliveryInfo),
            headers: { "Content-Type": "application/json" },
        });
        if (!response.ok) throw new Error("Failed to create delivery");
    },

    getOrderById: async (orderId) => {
        const response = await apiFetch(`/orders/${orderId}`);
        if (!response.ok) throw new Error("Failed to load order");

        const text = await response.text();
        if (!text) return null;                 
        return JSON.parse(text);
    }
};


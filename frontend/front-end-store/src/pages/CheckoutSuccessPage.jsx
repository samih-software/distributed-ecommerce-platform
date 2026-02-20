import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { OrderService } from "../services/OrderService";

function CheckoutSuccessPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const orderId = searchParams.get("orderId");

    const [order, setOrder] = useState(null);
    const [status, setStatus] = useState("VERIFYING");

    useEffect(() => {
        if (!orderId) {
            navigate("/home");
            return;
        }

        let interval;
        let attempts = 0;
        const maxAttempts = 22;

        const checkStatus = async () => {
            try {
                const data = await OrderService.getOrderById(orderId);

                if (data.status === "PAID") {
                    setOrder(data);
                    setStatus("PAID");
                    clearInterval(interval);
                } else {
                    attempts++;
                    if (attempts >= maxAttempts) {
                        setStatus("TIMEOUT");
                        clearInterval(interval);
                    }
                }
            } catch {
                setStatus("ERROR");
                clearInterval(interval);
            }
        };

        checkStatus();
        interval = setInterval(checkStatus, 2000);
        return () => clearInterval(interval);
    }, [orderId, navigate]);

    if (status === "VERIFYING")
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-gray-300">
                ⏳ Verifying your payment...
            </div>
        );

    if (status === "TIMEOUT")
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-yellow-400">
                Payment is still being confirmed. If you were charged, it will update shortly.
            </div>
        );

    if (status === "ERROR")
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-red-400">
                Could not verify payment. Please contact support if charged.
            </div>
        );

    return (
        <div className="bg-gray-900 min-h-screen text-white">
            <Navbar />
            <div className="px-6 py-12 max-w-3xl mx-auto text-center">
                <h1 className="text-3xl font-bold text-green-400 mb-6">Payment Confirmed!</h1>
                <p className="mb-6 text-gray-300">
                    Order ID: <span className="font-semibold">{order.id}</span>
                </p>

                <div className="bg-gray-800 p-6 rounded-lg border border-gray-700 text-left">
                    {order.items.map(item => (
                        <div key={item.product.id} className="flex justify-between mb-3 text-sm text-gray-300">
                            <span>{item.product.name} × {item.quantity}</span>
                            <span>€{item.totalPrice.toFixed(2)}</span>
                        </div>
                    ))}
                    <div className="border-t border-gray-700 mt-6 pt-4 font-semibold flex justify-between text-lg">
                        <span>Total</span>
                        <span>€{order.totalPrice.toFixed(2)}</span>
                    </div>
                </div>

                <button
                    onClick={() => navigate("/home")}
                    className="mt-8 h-11 px-6 rounded-md font-semibold bg-indigo-500 hover:bg-indigo-400 transition"
                >
                    Continue Shopping
                </button>
            </div>
        </div>
    );
}

export default CheckoutSuccessPage;

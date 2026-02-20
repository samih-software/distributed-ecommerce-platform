import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { OrderService } from "../services/OrderService";

function CheckoutPage() {
    const navigate = useNavigate();
    const [cartId, setCartId] = useState(null);
    const [cartItems, setCartItems] = useState([]);
    const [cartTotal, setCartTotal] = useState(0);
    const [loading, setLoading] = useState(true);
    const [deliveryInfo, setDeliveryInfo] = useState({
        recipientFirstName: "",
        recipientLastName: "",
        recipientEmail: "",
        recipientPhone: "",
        street: "",
        streetNumber: "",
        city: "",
        postalCode: "",
    });

    useEffect(() => {
        const savedCartId = localStorage.getItem("cartId");
        if (!savedCartId) {
            navigate("/home");
            return;
        }
        setCartId(savedCartId);
        loadCart(savedCartId);
    }, [navigate]);

    const loadCart = async (id) => {
        const cart = await OrderService.getCart(id);
        if (!cart.items.length) {
            navigate("/home");
            return;
        }
        setCartItems(cart.items);
        setCartTotal(cart.totalPrice);
        setLoading(false);
    };

    const handlePlaceOrder = async () => {
        for (const key in deliveryInfo)
            if (!deliveryInfo[key]) return alert("Please fill all fields");

        const result = await OrderService.checkout(cartId);
        await OrderService.createDelivery({ orderId: result.orderId, ...deliveryInfo });

        localStorage.removeItem("cartId");
        window.location.href = result.checkoutUrl;
    };

    if (loading)
        return <div className="bg-gray-900 min-h-screen flex items-center justify-center text-gray-300">Loading...</div>;

    return (
        <div className="bg-gray-900 min-h-screen text-white">
            <Navbar />
            <div className="px-6 py-12 lg:px-20 max-w-5xl mx-auto">
                <h1 className="text-3xl font-bold mb-10 text-center">Checkout</h1>

                <div className="grid lg:grid-cols-2 gap-8">
                    <div className="bg-gray-800 rounded-lg border border-gray-700 p-5">
                        <h2 className="text-lg font-semibold mb-5 border-b border-gray-700 pb-3">Order Summary</h2>
                        {cartItems.map(item => (
                            <div key={item.product.id} className="flex justify-between mb-3 text-sm text-gray-300">
                                <span>{item.product.name} × {item.quantity}</span>
                                <span>€{item.totalPrice.toFixed(2)}</span>
                            </div>
                        ))}
                        <div className="border-t border-gray-700 mt-6 pt-4 font-semibold flex justify-between text-lg">
                            <span>Total</span>
                            <span>€{cartTotal.toFixed(2)}</span>
                        </div>
                    </div>

                    <div className="bg-gray-800 rounded-lg border border-gray-700 p-5">
                        <h2 className="text-lg font-semibold mb-5 border-b border-gray-700 pb-3">Delivery Details</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {Object.keys(deliveryInfo).map(key => (
                                <input
                                    key={key}
                                    type="text"
                                    placeholder={key.replace(/recipient/, "").replace(/([A-Z])/g, " $1")}
                                    value={deliveryInfo[key]}
                                    onChange={e => setDeliveryInfo({ ...deliveryInfo, [key]: e.target.value })}
                                    className="bg-gray-900 border border-gray-700 rounded-md p-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                                />
                            ))}
                        </div>
                    </div>
                </div>

                <div className="flex justify-center mt-10">
                    <button
                        onClick={handlePlaceOrder}
                        className="h-11 px-6 rounded-md font-semibold bg-indigo-500 hover:bg-indigo-400 transition"
                    >
                        Confirm & Place Order
                    </button>
                </div>
            </div>
        </div>
    );
}

export default CheckoutPage;

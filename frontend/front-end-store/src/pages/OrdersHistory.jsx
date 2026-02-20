import React, { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { API_BASE_URL } from "../config";
import { OrderService } from "../services/OrderService";

function OrdersHistory() {
    const [orders, setOrders] = useState([]);
    const [expandedOrderId, setExpandedOrderId] = useState(null);
    const [deliveries, setDeliveries] = useState({});
    const [loadingDeliveryId, setLoadingDeliveryId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const data = await OrderService.getOrders();
                setOrders(data);
            } catch (err) {
                console.error(err);
                setError("Unable to load your orders");
            } finally {
                setLoading(false);
            }
        };
        loadOrders();
    }, []);

    const toggleOrder = async (orderId) => {
        if (expandedOrderId === orderId) {
            setExpandedOrderId(null);
            return;
        }

        setExpandedOrderId(orderId);

        if (!deliveries[orderId]) {
            try {
                setLoadingDeliveryId(orderId);
                const deliveryData = await OrderService.getDelivery(orderId);
                setDeliveries((prev) => ({ ...prev, [orderId]: deliveryData }));
            } catch (err) {
                console.error("Failed to load delivery info", err);
            } finally {
                setLoadingDeliveryId(null);
            }
        }
    };

    const formatDate = (date) => new Date(date).toLocaleDateString();
    const formatDateTime = (date) => new Date(date).toLocaleString();

    if (loading)
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-white">
                Loading orders...
            </div>
        );

    if (error)
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-red-400">
                {error}
            </div>
        );

    if (!orders.length)
        return (
            <div className="bg-gray-900 min-h-screen">
                <Navbar />
                <p className="text-gray-400 text-center py-20">You have not placed any orders yet.</p>
                <Footer />
            </div>
        );

    return (
        <div className="bg-gray-900 min-h-screen">
            <Navbar />
            <div className="px-6 py-12 lg:px-20">
                <h1 className="text-3xl font-bold text-white mb-8 text-center">My Orders</h1>

                <div className="space-y-6">
                    {orders.map((order) => {
                        const totalItems = order.items.reduce((sum, item) => sum + item.quantity, 0);
                        const delivery = deliveries[order.id];

                        return (
                            <div key={order.id} className="bg-gray-800 rounded-lg shadow-lg overflow-hidden">

                                <div
                                    className="p-4 flex justify-between items-center cursor-pointer hover:bg-gray-700 transition"
                                    onClick={() => toggleOrder(order.id)}
                                >
                                    <div className="text-white">
                                        <p className="font-semibold">Order #{order.id}</p>
                                        <p className="text-gray-400 text-sm">{formatDate(order.createdAt)}</p>
                                    </div>
                                    <div className="text-right text-white">
                                        <p>{totalItems} item{totalItems > 1 ? "s" : ""}</p>
                                        <p className="font-bold">€{order.totalPrice.toFixed(2)}</p>
                                    </div>
                                </div>


                                {expandedOrderId === order.id && (
                                    <div className="border-t border-gray-700 p-4 space-y-6">
                                        {loadingDeliveryId === order.id && (
                                            <p className="text-gray-400 text-sm">Loading delivery info...</p>
                                        )}

                                        {delivery && (
                                            <div className="bg-gray-700 p-4 rounded-lg text-white space-y-3">
                                                <p className="font-semibold text-lg">Delivery</p>
                                                <p className="text-sm">Status: {delivery.status}</p>

                                                {delivery.actualDeliveryTime ? (
                                                    <p className="text-sm text-green-400">Delivered on: {formatDateTime(delivery.actualDeliveryTime)}</p>
                                                ) : delivery.plannedEndTime ? (
                                                    <p className="text-sm text-blue-400">Expected delivery by: {formatDateTime(delivery.plannedEndTime)}</p>
                                                ) : delivery.scheduledDeliveryTime ? (
                                                    <p className="text-sm">Scheduled delivery: {formatDateTime(delivery.scheduledDeliveryTime)}</p>
                                                ) : null}

                                                <div className="text-sm text-gray-300 mt-2">
                                                    <p className="font-semibold text-white">Address</p>
                                                    <p>{delivery.street} {delivery.streetNumber}</p>
                                                    <p>{delivery.postalCode} {delivery.city}</p>
                                                </div>

                                                <div className="text-sm text-gray-300 mt-2">
                                                    <p className="font-semibold text-white">Recipient</p>
                                                    <p>{delivery.recipientFirstName} {delivery.recipientLastName}</p>
                                                    <p>{delivery.recipientPhone}</p>
                                                </div>
                                            </div>
                                        )}

                                        {order.items.map((item) => (
                                            <div key={item.product.id} className="flex items-center justify-between">
                                                <div className="flex items-center space-x-4">
                                                    <img
                                                        src={`${API_BASE_URL}/uploads/${item.product.imagePath}`}
                                                        alt={item.product.name}
                                                        className="w-16 h-16 object-cover rounded"
                                                    />
                                                    <div className="text-white">
                                                        <p className="font-semibold">{item.product.name}</p>
                                                        <p className="text-gray-400 text-sm">Quantity: {item.quantity}</p>
                                                    </div>
                                                </div>
                                                <div className="text-white font-semibold">€{item.totalPrice.toFixed(2)}</div>
                                            </div>
                                        ))}


                                        <div className="border-t border-gray-600 pt-4 flex justify-between text-white font-bold">
                                            <span>Total</span>
                                            <span>€{order.totalPrice.toFixed(2)}</span>
                                        </div>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default OrdersHistory;

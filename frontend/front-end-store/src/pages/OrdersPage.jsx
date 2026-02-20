import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { OrderService } from "../services/OrderService";
import { API_BASE_URL } from "../config";

function OrdersPage() {
    const navigate = useNavigate();

    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [cartId, setCartId] = useState(null);
    const [cartItems, setCartItems] = useState([]);
    const [cartTotal, setCartTotal] = useState(0);

    const itemsPerPage = 4;


    useEffect(() => {
        const loadProducts = async () => {
            try {
                const data = await OrderService.getProducts();
                setProducts(data);
            } catch {
                setError("Unable to load products");
            } finally {
                setLoading(false);
            }
        };
        loadProducts();
    }, []);


    useEffect(() => {
        const savedCartId = localStorage.getItem("cartId");
        if (savedCartId) {
            setCartId(savedCartId);
            loadCart(savedCartId);
        }
    }, []);

    const loadCart = async (id) => {
        try {
            const cart = await OrderService.getCart(id);
            setCartItems(cart.items);
            setCartTotal(cart.totalPrice);
        } catch {}
    };

    const handleOrder = async (item) => {
        let id = cartId;

        if (!id) {
            const cart = await OrderService.createCart();
            id = cart.id;
            setCartId(id);
            localStorage.setItem("cartId", id);
        }

        await OrderService.addItem(id, item.id);
        await loadCart(id);
    };

    const handleRemoveItem = async (productId) => {
        await OrderService.removeItem(cartId, productId);
        await loadCart(cartId);
    };

    const handleClearCart = async () => {
        await OrderService.clearCart(cartId);
        await loadCart(cartId);
    };

    const handleCheckout = () => {
        if (!cartId || cartItems.length === 0) return;
        navigate("/checkout");
    };

    if (loading)
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-white">
                Loading...
            </div>
        );

    if (error)
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-red-400">
                {error}
            </div>
        );

    const indexOfLastItem = currentPage * itemsPerPage;
    const currentItems = products.slice(indexOfLastItem - itemsPerPage, indexOfLastItem);
    const totalPages = Math.ceil(products.length / itemsPerPage);

    return (
        <div className="bg-gray-900 min-h-screen">
            <Navbar />

            <div className="px-6 py-12 lg:px-20">
                <h1 className="text-3xl font-bold text-white mb-8 text-center">
                    Our Fresh Fish Selection
                </h1>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">

                    <div className="lg:col-span-2 space-y-6">
                        {currentItems.map((item) => (
                            <div
                                key={item.id}
                                className="flex bg-gray-800 rounded-lg border border-gray-700 overflow-hidden"
                            >
                                <div className="w-1/4">
                                    <img
                                        src={`${API_BASE_URL}/uploads/${item.imagePath}`}
                                        alt={item.name}
                                        className="w-full h-full object-cover"
                                    />
                                </div>

                                <div className="w-3/4 p-4 text-white flex flex-col justify-between">
                                    <div>
                                        <h2 className="text-lg font-semibold">{item.name}</h2>
                                        <p className="text-gray-300 text-sm">
                                            €{Number(item.price).toFixed(2)}
                                        </p>
                                        <p className="mt-2 text-gray-400 text-sm">{item.description}</p>
                                    </div>

                                    <button
                                        onClick={() => handleOrder(item)}
                                        className="mt-4 h-9 px-3 text-sm rounded-md font-medium bg-indigo-500 hover:bg-indigo-400 transition"
                                    >
                                        Add to cart
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="bg-gray-800 rounded-lg border border-gray-700 p-4 text-white h-fit">
                        <h2 className="text-lg font-semibold mb-4">Your Cart</h2>

                        {cartItems.length === 0 && <p className="text-gray-400">Cart is empty</p>}

                        {cartItems.map((item) => (
                            <div key={item.product.id} className="flex justify-between mb-2 text-sm">
                <span>
                  {item.product.name} × {item.quantity}
                </span>
                                <div className="flex gap-2">
                                    <span>€{item.totalPrice.toFixed(2)}</span>
                                    <button
                                        onClick={() => handleRemoveItem(item.product.id)}
                                        className="h-8 px-2 text-xs rounded-md bg-red-500 hover:bg-red-400 transition"
                                    >
                                        Remove
                                    </button>
                                </div>
                            </div>
                        ))}

                        <div className="border-t border-gray-700 mt-4 pt-4 font-semibold flex justify-between">
                            <span>Total</span>
                            <span>€{cartTotal.toFixed(2)}</span>
                        </div>

                        <div className="mt-4 flex flex-col gap-2">
                            <button
                                onClick={handleClearCart}
                                className="h-9 px-3 text-sm rounded-md bg-gray-700 hover:bg-gray-600 transition"
                            >
                                Clear Cart
                            </button>

                            <button
                                onClick={handleCheckout}
                                disabled={cartItems.length === 0}
                                className="h-9 px-3 text-sm rounded-md font-medium bg-indigo-500 hover:bg-indigo-400 disabled:bg-gray-700 transition"
                            >
                                Checkout
                            </button>
                        </div>
                    </div>
                </div>


                <div className="flex justify-center mt-8 gap-2">
                    {Array.from({ length: totalPages }, (_, i) => (
                        <button
                            key={i}
                            onClick={() => setCurrentPage(i + 1)}
                            className={`px-3 py-1 rounded-md text-sm ${
                                currentPage === i + 1
                                    ? "bg-indigo-500 text-white"
                                    : "bg-gray-700 text-gray-300 hover:bg-gray-600"
                            }`}
                        >
                            {i + 1}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default OrdersPage;

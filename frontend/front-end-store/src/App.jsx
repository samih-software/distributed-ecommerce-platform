import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login.jsx";
import SignupPage from "./pages/SignupPage.jsx";
import ResetPasswordPage from "./pages/ResetPasswordPage.jsx";
import IntroPage from "./pages/Startpage.jsx";
import OrdersPage from "./pages/OrdersPage.jsx";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import PublicRoute from "./routes/PublicRoute.jsx";
import ProfilePage from "./pages/AccountPage.jsx";
import OrdersHistory from "./pages/OrdersHistory.jsx";
import CheckoutPage from "./pages/CheckoutPage.jsx";
import CheckoutSuccessPage from "./pages/CheckoutSuccessPage.jsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/welcome" replace />} />

                <Route path="/welcome" element={<IntroPage />} />


                <Route
                    path="/login"
                    element={
                        <PublicRoute>
                            <Login />
                        </PublicRoute>
                    }
                />
                <Route
                    path="/signup"
                    element={
                        <PublicRoute>
                            <SignupPage />
                        </PublicRoute>
                    }
                />
                <Route
                    path="/forgot-password"
                    element={
                        <PublicRoute>
                            <ResetPasswordPage />
                        </PublicRoute>
                    }
                />


                <Route
                    path="/home"
                    element={
                        <ProtectedRoute>
                            <OrdersPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/orders"
                    element={
                        <ProtectedRoute>
                            <OrdersHistory />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/checkout"
                    element={
                        <ProtectedRoute>
                            <CheckoutPage />
                        </ProtectedRoute>
                    }
                />


                <Route
                    path="/checkout-success"
                    element={
                        <ProtectedRoute>
                            <CheckoutSuccessPage />
                        </ProtectedRoute>
                    }
                />




                <Route
                    path="/account"
                    element={
                        <ProtectedRoute>
                            <ProfilePage />
                        </ProtectedRoute>
                    }
                />


            </Routes>
        </Router>
    );
}

export default App;

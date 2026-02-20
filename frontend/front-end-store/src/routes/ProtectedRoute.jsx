import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/AuthService";
import React from "react";

function ProtectedRoute({ children }) {
    const [isAuthenticated, setIsAuthenticated] = useState(null);

    useEffect(() => {
        const checkAuth = async () => {

            if (AuthService.getAccessToken()) {
                setIsAuthenticated(true);
                return;
            }


            const refreshed = await AuthService.refreshToken();
            setIsAuthenticated(refreshed);
        };

        checkAuth();
    }, []);


    if (isAuthenticated === null) {
        return (
            <div className="bg-gray-900 min-h-screen flex items-center justify-center text-white">
                Checking authentication...
            </div>
        );
    }


    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }


    return children;
}

export default ProtectedRoute;

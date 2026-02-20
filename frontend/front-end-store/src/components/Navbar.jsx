import React from "react";
import { Link } from "react-router-dom";
import { FaBoxOpen, FaClipboardList, FaUserCircle, FaSignOutAlt } from "react-icons/fa";

function Navbar() {
    return (
        <nav className="bg-gray-900 text-white shadow-md">
            <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">

                <Link to="/" className="flex items-center space-x-2">
                    <img
                        src="src/pictures/logo.png"
                        alt="Mareno & Marena"
                        className="h-10 w-10 rounded-full object-cover"
                    />
                    <span className="font-bold text-xl">Mareno & Marena</span>
                </Link>


                <div className="flex space-x-6 items-center">
                    <Link
                        to="/home"
                        className="flex items-center space-x-1 hover:text-indigo-400"
                    >
                        <FaBoxOpen /> <span>Home</span>
                    </Link>
                    <Link
                        to="/orders"
                        className="flex items-center space-x-1 hover:text-indigo-400"
                    >
                        <FaClipboardList /> <span>Orders</span>
                    </Link>
                    <Link
                        to="/account"
                        className="flex items-center space-x-1 hover:text-indigo-400"
                    >
                        <FaUserCircle /> <span>Account</span>
                    </Link>
                    <button className="flex items-center space-x-1 hover:text-red-400">
                        <FaSignOutAlt /> <span>Logout</span>
                    </button>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;

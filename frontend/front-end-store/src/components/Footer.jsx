import React from "react";
import { Link } from "react-router-dom";
import { FaFacebookF, FaInstagram, FaTiktok } from "react-icons/fa";

function Footer() {
    return (
        <footer className="py-12 px-6 lg:px-8 bg-gray-900 border-t border-gray-700">
            <div className="max-w-6xl mx-auto">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-8">

                    <div className="text-center md:text-left">
                        <h3 className="text-xl font-bold text-white mb-4">Mareno & Marena</h3>
                        <p className="text-gray-400 text-sm leading-relaxed">
                            Delivering fresh, sustainable fish to restaurants and businesses across the Benelux. Quality and eco-friendliness at the heart of everything we do.
                        </p>
                    </div>


                    <div className="text-center md:text-left">
                        <h4 className="text-lg font-semibold text-indigo-400 mb-4">Quick Links</h4>
                        <ul className="space-y-2">
                            <li>
                                <Link to="/about" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    About Us
                                </Link>
                            </li>
                            <li>
                                <Link to="/services" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    Our Services
                                </Link>
                            </li>
                            <li>
                                <Link to="/contact" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    Contact
                                </Link>
                            </li>
                            <li>
                                <Link to="/faq" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    FAQ
                                </Link>
                            </li>
                        </ul>
                    </div>


                    <div className="text-center md:text-left">
                        <h4 className="text-lg font-semibold text-indigo-400 mb-4">Legal</h4>
                        <ul className="space-y-2">
                            <li>
                                <Link to="/privacy-policy" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    Privacy Policy
                                </Link>
                            </li>
                            <li>
                                <Link to="/terms-of-service" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    Terms of Service
                                </Link>
                            </li>
                            <li>
                                <Link to="/sustainability" className="text-gray-400 hover:text-indigo-300 transition duration-200">
                                    Sustainability
                                </Link>
                            </li>
                        </ul>
                    </div>


                    <div className="text-center md:text-left">
                        <h4 className="text-lg font-semibold text-indigo-400 mb-4">Connect With Us</h4>
                        <p className="text-gray-400 text-sm mb-4">
                            Email: <a href="mailto:info@mareno-marena.com" className="hover:text-indigo-300">info@mareno-marena.com</a><br />
                            Phone: <a href="tel:+31123456789" className="hover:text-indigo-300">+31 123 456 789</a>
                        </p>
                        <div className="flex justify-center md:justify-start space-x-4">
                            <a href="#" className="text-indigo-400 hover:text-indigo-300 transition duration-200 text-2xl">
                                <FaFacebookF />
                            </a>
                            <a href="#" className="text-indigo-400 hover:text-indigo-300 transition duration-200 text-2xl">
                                <FaInstagram />
                            </a>
                            <a href="#" className="text-indigo-400 hover:text-indigo-300 transition duration-200 text-2xl">
                                <FaTiktok />
                            </a>
                        </div>
                    </div>
                </div>


                <div className="mt-8 pt-8 border-t border-gray-700 flex flex-col md:flex-row justify-between items-center">
                    <p className="text-gray-400 text-sm">
                        © 2026 Mareno & Marena. All rights reserved.
                    </p>
                    <Link
                        to="/signup"
                        className="mt-4 md:mt-0 inline-block rounded-lg bg-gradient-to-r from-indigo-500 to-indigo-600 px-6 py-2 text-white font-semibold hover:from-indigo-600 hover:to-indigo-700 transition-all duration-300 shadow-lg hover:shadow-xl transform hover:scale-105"
                    >
                        Start Ordering Today
                    </Link>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
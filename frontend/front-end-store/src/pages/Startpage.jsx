import React from "react";
import { Link } from "react-router-dom";
import { FaFacebookF, FaInstagram, FaTiktok } from "react-icons/fa";
import Footer from "../components/Footer";
import WhatWeDo from "../components/WhatWeDo";

function IntroPage() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-white">

            <div className="flex min-h-screen flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-4xl">

                    <div className="flex flex-col md:flex-row items-center justify-center md:space-x-8 text-center md:text-left">
                        <img
                            src="src/pictures/logo.png"
                            alt="Mareno & Marena"
                            className="h-40 w-40 rounded-full object-cover shadow-2xl ring-4 ring-indigo-500/50 mb-6 md:mb-0"
                        />
                        <div>
                            <h1 className="text-4xl font-bold tracking-tight text-white drop-shadow-lg mb-4">
                                Mareno & Marena
                            </h1>
                            <p className="text-lg text-gray-300 leading-relaxed max-w-md">
                                Fresh fish delivered straight to your restaurant in the Benelux.
                                Order today and elevate your menu with high-quality, sustainable seafood that delights your customers.
                            </p>
                        </div>
                    </div>

                    <img
                        src="src/pictures/people.png"
                        alt="Our team preparing fresh fish for delivery"
                        className="mx-auto mt-8 h-80 w-full max-w-lg rounded-xl object-cover shadow-2xl border-4 border-indigo-500/30"
                    />
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-lg">
                    <div className="grid gap-6 bg-gray-800/70 backdrop-blur-sm rounded-xl p-8 shadow-2xl border border-gray-700">
                        <div className="text-center text-white">
                            <h2 className="text-2xl font-semibold">Why Choose Us?</h2>
                            <p className="mt-3 text-gray-300 leading-relaxed">
                                We prioritize sustainability and top-quality fish. Our clients consistently rate us 4.7 stars for unmatched freshness, reliability, and service that transforms their menus.
                            </p>
                        </div>

                        <div className="flex justify-around mt-6">
                            <div className="text-center">
                                <p className="text-3xl font-bold text-yellow-400 drop-shadow">4.7 ★★★★☆</p>
                                <p className="text-gray-300 text-sm mt-1">Top rated by 50+ restaurant clients</p>
                            </div>
                            <div className="text-center">
                                <p className="text-3xl font-bold text-green-400">🌿</p>
                                <p className="text-gray-300 text-sm mt-1">Committed to sustainability</p>
                            </div>
                            <div className="text-center">
                                <p className="text-3xl font-bold text-blue-400">🐟</p>
                                <p className="text-gray-300 text-sm mt-1">High-quality fresh fish</p>
                            </div>
                        </div>

                        <div className="mt-8 flex justify-center">
                            <Link
                                to="/signup"
                                className="rounded-lg bg-gradient-to-r from-indigo-500 to-indigo-600 px-8 py-3 text-white font-semibold hover:from-indigo-600 hover:to-indigo-700 transition-all duration-300 shadow-lg hover:shadow-xl transform hover:scale-105"
                            >
                                Get Started Today
                            </Link>
                        </div>
                    </div>
                </div>

                <p className="mt-10 text-center text-sm text-gray-400">
                    Already have an account?{" "}
                    <Link to="/login" className="font-semibold text-indigo-400 hover:text-indigo-300 transition duration-200">
                        Sign in
                    </Link>
                </p>
            </div>


            <WhatWeDo />


            <section className="py-20 px-6 lg:px-8 bg-gradient-to-br from-gray-900 to-gray-800">
                <div className="max-w-5xl mx-auto text-center">
                    <h2 className="text-3xl font-bold text-white mb-6 drop-shadow">Our Beliefs</h2>
                    <p className="text-lg text-gray-300 mb-10 leading-relaxed max-w-3xl mx-auto">
                        We believe in quality above all. Every fish we deliver is sourced sustainably, ensuring respect for the environment and the communities that depend on it. Our commitment to traceability and eco-friendly practices sets us apart as the trusted choice for conscious businesses. Join us in making a positive impact.
                    </p>
                    <div className="grid md:grid-cols-2 gap-10">
                        <div className="text-left bg-gray-800/50 p-6 rounded-xl shadow-lg">
                            <h3 className="text-xl font-semibold text-indigo-400 mb-4">🐟 Quality First</h3>
                            <p className="text-gray-300 leading-relaxed">
                                We inspect every batch for freshness and flavor, guaranteeing only the best reaches your door. Your customers deserve nothing less.
                            </p>
                        </div>
                        <div className="text-left bg-gray-800/50 p-6 rounded-xl shadow-lg">
                            <h3 className="text-xl font-semibold text-indigo-400 mb-4">🌿 Environmental Respect</h3>
                            <p className="text-gray-300 leading-relaxed">
                                Partnering with certified sustainable suppliers to protect oceans and promote responsible fishing. Choose us for a greener future.
                            </p>
                        </div>
                    </div>
                </div>
            </section>


            <section className="py-20 px-6 lg:px-8 bg-gray-800/50">
                <div className="max-w-5xl mx-auto text-center">
                    <h2 className="text-3xl font-bold text-white mb-6 drop-shadow">What Our Customers Say</h2>
                    <p className="text-lg text-gray-300 mb-10 leading-relaxed">
                        Hear from restaurants, cafes, and stores who trust Mareno & Marena as their only source for quality fish delivery. Don't just take our word, see why we're the exclusive choice.
                    </p>
                    <div className="grid md:grid-cols-2 gap-8">
                        <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                            <div className="flex items-center justify-center mb-4">
                                <span className="text-yellow-400 text-lg">★★★★★</span>
                            </div>
                            <p className="text-gray-300 italic mb-4 leading-relaxed">
                                "Mareno & Marena is our go-to for fresh fish. Their quality is unmatched, and we trust them for sustainable sourcing. As a cafe, we wouldn't dream of using anyone else!"
                            </p>
                            <p className="text-indigo-400 font-semibold">- Café de Zee, Amsterdam</p>
                        </div>
                        <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                            <div className="flex items-center justify-center mb-4">
                                <span className="text-yellow-400 text-lg">★★★★☆</span>
                            </div>
                            <p className="text-gray-300 italic mb-4 leading-relaxed">
                                "Only Mareno & Marena delivers the premium fish we need for our restaurant. Our customers rave about the freshness— they're the best in the Benelux!"
                            </p>
                            <p className="text-indigo-400 font-semibold">- Restaurant Ocean, Brussels</p>
                        </div>
                        <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                            <div className="flex items-center justify-center mb-4">
                                <span className="text-yellow-400 text-lg">★★★★★</span>
                            </div>
                            <p className="text-gray-300 italic mb-4 leading-relaxed">
                                "As a store, we rely on Mareno & Marena for consistent, high-quality deliveries. Their commitment to sustainability makes them our exclusive choice."
                            </p>
                            <p className="text-indigo-400 font-semibold">- Store Vissen, Rotterdam</p>
                        </div>
                        <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                            <div className="flex items-center justify-center mb-4">
                                <span className="text-yellow-400 text-lg">★★★★☆</span>
                            </div>
                            <p className="text-gray-300 italic mb-4 leading-relaxed">
                                "Mareno & Marena's fish is always top-notch. We've tried others, but nothing compares to their quality and eco-friendly practices. Highly recommend!"
                            </p>
                            <p className="text-indigo-400 font-semibold">- Bistro Zeezicht, Antwerp</p>
                        </div>
                    </div>
                </div>
            </section>


            <Footer />
        </div>
    );
}

export default IntroPage;
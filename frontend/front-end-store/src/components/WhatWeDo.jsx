import React from "react";

function WhatWeDo() {
    return (
        <section className="py-20 px-6 lg:px-8 bg-gray-800/50">
            <div className="max-w-5xl mx-auto text-center">
                <h2 className="text-3xl font-bold text-white mb-6 drop-shadow">What We Do</h2>
                <p className="text-lg text-gray-300 mb-10 leading-relaxed max-w-3xl mx-auto">
                    At Mareno & Marena, we specialize in delivering premium, fresh fish directly to restaurants, stores, and food businesses across the Benelux. Our streamlined ordering system ensures timely deliveries, allowing you to focus on serving exceptional meals without compromising on quality. Experience the difference that sets us apart.
                </p>
                <div className="grid md:grid-cols-3 gap-8">
                    <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                        <div className="text-5xl mb-4">🚚</div>
                        <h3 className="text-xl font-semibold text-indigo-400">Finding the Optimal Delivery and Reducing CO2</h3>
                        <p className="text-gray-300 mt-3 leading-relaxed">We optimize routes for efficient, eco-friendly deliveries, minimizing CO2 emissions while ensuring your orders arrive fresh and on time.</p>
                    </div>
                    <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                        <div className="text-5xl mb-4">📦</div>
                        <h3 className="text-xl font-semibold text-indigo-400">Custom Orders</h3>
                        <p className="text-gray-300 mt-3 leading-relaxed">Variety of fish species and quantities to meet your unique needs.</p>
                    </div>
                    <div className="bg-gradient-to-br from-gray-700 to-gray-600 p-8 rounded-xl shadow-lg hover:shadow-xl transition duration-300">
                        <div className="text-5xl mb-4">📞</div>
                        <h3 className="text-xl font-semibold text-indigo-400">Dedicated Support</h3>
                        <p className="text-gray-300 mt-3 leading-relaxed">Personalized service from our expert team, ready to assist.</p>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default WhatWeDo;
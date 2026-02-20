import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiFetch } from "../services/api";

function SignupPage() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        bio: "",
        phone: "",
        birthday: "",
    });

    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
        setErrors({ ...errors, [name]: "", general: "" });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newErrors = {};
        if (!form.name) newErrors.name = "Name is required";
        if (!form.email) newErrors.email = "Email is required";
        if (!form.password) newErrors.password = "Password is required";
        else if (form.password.length < 6)
            newErrors.password = "Password must be at least 6 characters";

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            setLoading(true);


            const response = await apiFetch(
                "/users",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        name: form.name,
                        email: form.email,
                        password: form.password,
                    }),
                },
                true
            );

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || "User creation failed");
            }


            navigate("/login");

        } catch (err) {
            setErrors({ general: err.message });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex min-h-screen flex-col justify-center px-6 py-12 lg:px-8 bg-gray-900">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <h2 className="text-center text-2xl font-bold text-white">
                    Create your account
                </h2>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <form onSubmit={handleSubmit} className="space-y-6">


                    <div>
                        <input
                            type="text"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            placeholder="Name"
                            className="w-full rounded-md bg-white/5 px-3 py-2 text-white"
                        />
                        {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
                    </div>


                    <div>
                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            placeholder="Email"
                            className="w-full rounded-md bg-white/5 px-3 py-2 text-white"
                        />
                        {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
                    </div>


                    <div>
                        <input
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            placeholder="Password"
                            className="w-full rounded-md bg-white/5 px-3 py-2 text-white"
                        />
                        {errors.password && <p className="text-red-500 text-sm">{errors.password}</p>}
                    </div>

                    {errors.general && (
                        <p className="text-red-500 text-sm text-center">
                            {errors.general}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full rounded-md bg-indigo-500 py-2 text-white font-semibold hover:bg-indigo-400"
                    >
                        {loading ? "Creating..." : "Create Account"}
                    </button>
                </form>

                <p className="mt-6 text-center text-sm text-gray-400">
                    Already have an account?{" "}
                    <Link to="/login" className="text-indigo-400 hover:text-indigo-300">
                        Sign in
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default SignupPage;

import React, { useState } from "react";

function ResetPasswordPage() {
    const [step, setStep] = useState(1);
    const [email, setEmail] = useState("");
    const [code, setCode] = useState(new Array(6).fill(""));
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");


    const handleCodeChange = (e, index) => {
        const value = e.target.value.replace(/\D/, ""); // only digits
        const newCode = [...code];
        newCode[index] = value;
        setCode(newCode);


        if (value && index < 5) {
            const nextInput = document.getElementById(`code-${index + 1}`);
            if (nextInput) nextInput.focus();
        }
    };

    const handleSubmitEmail = (e) => {
        e.preventDefault();
        if (!email) {
            setError("Email is required");
            return;
        }
        setError("");

        console.log("Verification code sent to:", email);
        setStep(2);
    };

    const handleSubmitCode = (e) => {
        e.preventDefault();
        const enteredCode = code.join("");
        if (enteredCode !== "111111") {
            setError("Invalid verification code");
            return;
        }
        setError("");
        setStep(3);
    };

    const handleSubmitPassword = (e) => {
        e.preventDefault();
        if (!password || password.length < 6) {
            setError("Password must be at least 6 characters");
            return;
        }
        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }
        setError("");
        console.log("Password changed for:", email);
        alert("Password changed successfully! You can now login.");
        setStep(1); // reset flow
        setEmail("");
        setCode(new Array(6).fill(""));
        setPassword("");
        setConfirmPassword("");
    };

    return (
        <div className="flex min-h-screen flex-col justify-center px-6 py-12 lg:px-8 bg-gray-900">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <h2 className="text-center text-2xl font-bold text-white mb-2">
                    Forgot Password
                </h2>
                <p className="text-center text-sm text-gray-400">
                    {step === 1 && "Enter your email to receive a verification code"}
                    {step === 2 && "Enter the 6-digit code sent to your email"}
                    {step === 3 && "Enter a new password"}
                </p>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                {step === 1 && (
                    <form onSubmit={handleSubmitEmail} className="space-y-6">
                        <div>
                            <label htmlFor="email" className="block text-sm font-medium text-gray-100">
                                Email
                            </label>
                            <input
                                type="email"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="you@example.com"
                                className="mt-2 block w-full rounded-md bg-white/5 px-3 py-1.5 text-base text-white outline-1 -outline-offset-1 placeholder:text-gray-500 focus:outline-2 sm:text-sm"
                            />
                            {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
                        </div>
                        <button
                            type="submit"
                            className="flex w-full justify-center rounded-md bg-indigo-500 px-3 py-1.5 text-sm font-semibold text-white hover:bg-indigo-400 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500"
                        >
                            Send Verification Code
                        </button>
                    </form>
                )}

                {step === 2 && (
                    <form onSubmit={handleSubmitCode} className="space-y-6">
                        <div className="flex justify-between space-x-2">
                            {code.map((c, i) => (
                                <input
                                    key={i}
                                    id={`code-${i}`}
                                    type="text"
                                    maxLength={1}
                                    value={c}
                                    onChange={(e) => handleCodeChange(e, i)}
                                    className="w-12 h-12 text-center text-lg rounded-md bg-white/5 text-white outline-1 -outline-offset-1 placeholder:text-gray-500 focus:outline-2"
                                />
                            ))}
                        </div>
                        {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
                        <button
                            type="submit"
                            className="flex w-full justify-center rounded-md bg-indigo-500 px-3 py-1.5 text-sm font-semibold text-white hover:bg-indigo-400 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500"
                        >
                            Verify Code
                        </button>
                    </form>
                )}

                {step === 3 && (
                    <form onSubmit={handleSubmitPassword} className="space-y-6">
                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-gray-100">
                                New Password
                            </label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="********"
                                className="mt-2 block w-full rounded-md bg-white/5 px-3 py-1.5 text-base text-white outline-1 -outline-offset-1 placeholder:text-gray-500 focus:outline-2 sm:text-sm"
                            />
                        </div>
                        <div>
                            <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-100">
                                Confirm Password
                            </label>
                            <input
                                type="password"
                                id="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                placeholder="********"
                                className="mt-2 block w-full rounded-md bg-white/5 px-3 py-1.5 text-base text-white outline-1 -outline-offset-1 placeholder:text-gray-500 focus:outline-2 sm:text-sm"
                            />
                        </div>
                        {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
                        <button
                            type="submit"
                            className="flex w-full justify-center rounded-md bg-indigo-500 px-3 py-1.5 text-sm font-semibold text-white hover:bg-indigo-400 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500"
                        >
                            Change Password
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
}

export default ResetPasswordPage;

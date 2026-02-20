import React, { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import { UserService } from "../services/UserService";

function ProfilePage() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [saving, setSaving] = useState(false);
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [changingPassword, setChangingPassword] = useState(false);

    useEffect(() => {
        const loadUser = async () => {
            try {
                const data = await UserService.getProfile();
                setUser(data);
            } catch (err) {
                console.error(err);
                setError("Unable to load user profile");
            } finally {
                setLoading(false);
            }
        };
        loadUser();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUser(prev => ({ ...prev, [name]: value }));
    };

    const handleSave = async () => {
        if (!user) return;
        setSaving(true);
        try {
            await UserService.updateProfile(user.id, { name: user.name, email: user.email });
            alert("Profile updated successfully!");
        } catch (err) {
            console.error(err);
            alert("Error updating profile");
        } finally {
            setSaving(false);
        }
    };

    const handleChangePassword = async () => {
        if (!user || !currentPassword || !newPassword) return alert("Please fill both fields");
        setChangingPassword(true);
        try {
            await UserService.changePassword(user.id, currentPassword, newPassword);
            alert("Password changed successfully!");
            setCurrentPassword("");
            setNewPassword("");
        } catch (err) {
            console.error(err);
            alert("Error changing password");
        } finally {
            setChangingPassword(false);
        }
    };

    if (loading)
        return <div className="bg-gray-900 min-h-screen flex items-center justify-center text-white">Loading profile...</div>;
    if (error)
        return <div className="bg-gray-900 min-h-screen flex items-center justify-center text-red-400">{error}</div>;

    return (
        <div className="bg-gray-900 min-h-screen">
            <Navbar />
            <div className="px-6 py-12 lg:px-20 max-w-xl mx-auto text-white">
                <h1 className="text-3xl font-bold mb-8 text-center">Profile</h1>

                <div className="space-y-4">
                    <div>
                        <label className="block text-gray-300 mb-1">Name</label>
                        <input type="text" name="name" value={user.name} onChange={handleChange} className="w-full p-2 rounded bg-gray-700 text-white" />
                    </div>
                    <div>
                        <label className="block text-gray-300 mb-1">Email</label>
                        <input type="email" name="email" value={user.email} onChange={handleChange} className="w-full p-2 rounded bg-gray-700 text-white" />
                    </div>
                    <button onClick={handleSave} disabled={saving} className="bg-indigo-500 px-4 py-2 rounded hover:bg-indigo-400 mt-4 w-full font-semibold">
                        {saving ? "Saving..." : "Save Changes"}
                    </button>
                </div>

                <h2 className="text-2xl font-semibold mt-8 mb-4">Change Password</h2>
                <div className="space-y-4">
                    <div>
                        <label className="block text-gray-300 mb-1">Current Password</label>
                        <input type="password" value={currentPassword} onChange={e => setCurrentPassword(e.target.value)} className="w-full p-2 rounded bg-gray-700 text-white" />
                    </div>
                    <div>
                        <label className="block text-gray-300 mb-1">New Password</label>
                        <input type="password" value={newPassword} onChange={e => setNewPassword(e.target.value)} className="w-full p-2 rounded bg-gray-700 text-white" />
                    </div>
                    <button onClick={handleChangePassword} disabled={changingPassword} className="bg-red-500 px-4 py-2 rounded hover:bg-red-400 w-full font-semibold">
                        {changingPassword ? "Changing..." : "Change Password"}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;

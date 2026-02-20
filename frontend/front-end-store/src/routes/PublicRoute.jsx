import { Navigate } from "react-router-dom";
import AuthService from "../services/AuthService";

function PublicRoute({ children }) {

    if (AuthService.getAccessToken()) {
        return <Navigate to="/home" replace />;
    }

    return children;
}

export default PublicRoute;

// src/components/ProtectedRoute.tsx
import React, {JSX} from "react";
import { useSelector } from "react-redux";
import { RootState } from "../store/store";
import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
    children: JSX.Element;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
    const token = useSelector((state: RootState) => state.auth.token);
    if (!token) {
        return <Navigate to="/login" />;
    }
    return children;
};

export default ProtectedRoute;

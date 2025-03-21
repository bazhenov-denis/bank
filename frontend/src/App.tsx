// src/App.tsx
import React from "react";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from "./component/pages/LoginPage.tsx";
import RegisterPage from "./component/pages/RegisterPage.tsx";
import ProtectedRoute from "./component/ProtectedRoute.tsx";
import {MainGrid} from "./component/MainGrid.tsx";

const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route
                    path="/finance"
                    element={
                        <ProtectedRoute>
                            <MainGrid></MainGrid>
                        </ProtectedRoute>
                    }
                />
                {/* Если маршрут не найден – редирект на логин */}
                <Route path="*" element={<LoginPage />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;

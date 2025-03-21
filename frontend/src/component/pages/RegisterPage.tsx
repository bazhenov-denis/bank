// src/pages/RegisterPage.tsx
import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { register } from "../../store/AuthSlice.ts";
import { AppDispatch } from "../../store/store.ts";
import { useNavigate, Link } from "react-router-dom";

import "./page.css"

const RegisterPage: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await dispatch(register({ username, password })).unwrap();
            navigate("/login"); // Перенаправление на страницу входа после успешной регистрации
        } catch (err) {
            setError("Ошибка при регистрации. Попробуйте ещё раз.");
        }
    };

    return (
        <div className="login-box">
            <div className="page-container-input">
                <h2>Регистрация</h2>
                {error && <p style={{color: "red"}}>{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div className="page-form">
                        <label>Имя пользователя:</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="page-form">
                        <label>Пароль:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit">Зарегистрироваться</button>
                </form>
                <p>
                    Уже есть аккаунт? <Link to="/login">Войти</Link>
                </p>
            </div>
        </div>
            );
            };

            export default RegisterPage;

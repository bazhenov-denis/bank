import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { login } from "../../store/AuthSlice.ts";
import { AppDispatch } from "../../store/store.ts";
import { useNavigate, Link } from "react-router-dom";
import "./page.css";

const LoginPage: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await dispatch(login({ username, password })).unwrap();
            navigate("/finance"); // Перенаправление на основную страницу после успешного входа
        } catch (error) {
            setError("Ошибка при входе. Проверьте данные.");
        }
    };

    return (
        <div className="login-box">
            <div className="page-container-input">
                <h2>Вход</h2>
                {error && <p style={{ color: "red" }}>{error}</p>}
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
                    <button type="submit">Войти</button>
                </form>
                <p>
                    Нет аккаунта? <Link to="/register">Зарегистрироваться</Link>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;

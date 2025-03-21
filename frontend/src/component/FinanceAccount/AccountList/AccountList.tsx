// src/components/FinanceAccount/AccountList/AccountList.tsx
import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { RootState, AppDispatch } from "../../../store/store";
import { fetchAccounts, addAccount, removeAccount } from "../../../store/AccountSlice.ts";
import "./Wallet.css";

const AccountList: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const accounts = useSelector((state: RootState) => state.accounts.accounts);
    console.log(accounts);
    const status = useSelector((state: RootState) => state.accounts.status);
    const [accountName, setAccountName] = useState<string>("");

    // Загружаем счета при первом рендере
    useEffect(() => {
        dispatch(fetchAccounts());
    }, [dispatch]);

    // Создание нового счета
    const handleCreateAccount = () => {
        if (!accountName.trim()) {
            alert("Введите название счета");
            return;
        }
        dispatch(addAccount(accountName));
        setAccountName("");
    };

    // Удаление счета
    const handleDelete = (id: number) => {
        dispatch(removeAccount(id));
    };
    const sortedAccounts = [...accounts].sort((a, b) => a.id - b.id);

    return (
        <div className="wallet-container">
            <div className="wallet-header">
                <div className="wallet-title">
                    {/* Левая стрелка (назад) и текст */}
                    <span className="wallet-back-arrow">&larr;</span>
                    <h2>Кошелёк</h2>
                </div>
            </div>

            <div className="wallet-cards">
                {status === "loading" ? (
                    <p className="loading-text">Загрузка счетов...</p>
                ) : accounts.length === 0 ? (
                    <div className="wallet-card">
                        <div className="wallet-card-info">
                            <div className="wallet-details">Счетов нет</div>
                        </div>
                    </div>
                ) : (
                    sortedAccounts.map((acc) => (
                        <div className="wallet-card-wrapper" key={acc.id}>
                            <div className="wallet-card">
                                <div className="wallet-icon">
                                    <i className="fas fa-credit-card"></i>
                                </div>
                                <div className="wallet-card-info">
                                    <div className="wallet-amount">{acc.balance}</div>
                                    <div className="wallet-details">{acc.name}</div>
                                    <button onClick={() => handleDelete(acc.id)}>Удалить</button>
                                </div>
                            </div>
                        </div>
                    ))
                )}

                {/* Добавление нового счета */}
                <div className="wallet-card-wrapper">
                    <div className="wallet-card add-account">
                        <input
                            type="text"
                            placeholder="Название счета"
                            value={accountName}
                            onChange={(e) => setAccountName(e.target.value)}
                        />
                        <button onClick={handleCreateAccount}>+</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AccountList;

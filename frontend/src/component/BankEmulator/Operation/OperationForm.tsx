// src/components/BankEmulator/Operation/OperationForm.tsx
import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState, AppDispatch } from "../../../store/store";
import { addOperation } from "../../../store/OperationSlice.ts";
import { fetchAccounts } from "../../../store/AccountSlice.ts";
import { getAllCategories } from "../../../api/api";
import "./OperationForm.css";

const OperationForm: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();

    const accounts = useSelector((state: RootState) => state.accounts.accounts);

    const [categories, setCategories] = useState<{ id: number; name: string; type: "INCOME" | "EXPENSE" }[]>([]);

    const [selectedAccount, setSelectedAccount] = useState<number | "">("");
    const [selectedCategory, setSelectedCategory] = useState<number | "">("");
    const [operationType, setOperationType] = useState<"INCOME" | "EXPENSE" | "">("");
    const [amount, setAmount] = useState<string>("");
    const [description, setDescription] = useState<string>("");

    // Загружаем категории при рендере
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const data = await getAllCategories();
                setCategories(data);
            } catch (error) {
                console.error("Ошибка при загрузке категорий:", error);
            }
        };
        fetchCategories();
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedAccount || !selectedCategory) {
            alert("Выберите счёт и категорию");
            return;
        }

        const numericAmount = parseFloat(amount);
        if (isNaN(numericAmount) || numericAmount <= 0) {
            alert("Введите сумму больше нуля");
            return;
        }

        const account = accounts.find(acc => acc.id === Number(selectedAccount));

        if (!account) {
            alert("Ошибка: не найден счёт");
            return;
        }

        dispatch(
            addOperation({
                type: operationType as "INCOME" | "EXPENSE",
                amount: numericAmount,
                description,
                date: new Date().toISOString().slice(0, 10),
                bankAccount: account,
                categoryId: Number(selectedCategory),
            })
        );

        // После операции обновляем счета
        dispatch(fetchAccounts());

        // Очищаем форму после успешного выполнения
        setSelectedAccount("");
        setSelectedCategory("");
        setOperationType("");
        setAmount("");
        setDescription("");
    };

    return (
        <div className="operation-form-container">
            <h2>Новая операция</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Счёт:</label>
                    <select value={selectedAccount} onChange={(e) => setSelectedAccount(Number(e.target.value))}>
                        <option value="" disabled>Выберите счёт</option>
                        {Array.isArray(accounts) ? (
                            accounts.map((account) => (
                                <option key={account.id} value={account.id}>
                                    {account.name} — Баланс: {account.balance}
                                </option>
                            ))
                        ) : (
                            <option disabled>Нет счетов</option>
                        )}
                    </select>
                </div>
                <div className="form-group">
                    <label>Тип операции:</label>
                    <select value={operationType} onChange={(e) => setOperationType(e.target.value as "INCOME" | "EXPENSE" | "")}>
                        <option value="" disabled>Выберите тип операции</option>
                        <option value="INCOME">Пополнение</option>
                        <option value="EXPENSE">Трата</option>
                    </select>
                </div>
                {operationType && (
                    <div className="form-group">
                        <label>Категория:</label>
                        <select value={selectedCategory} onChange={(e) => setSelectedCategory(Number(e.target.value))}>
                            <option value="" disabled>Выберите категорию</option>
                            {categories
                                .filter((cat) => cat.type === operationType)
                                .map((cat) => (
                                    <option key={cat.id} value={cat.id}>
                                        {cat.name}
                                    </option>
                                ))}
                        </select>
                    </div>
                )}
                <div className="form-group">
                    <label>Сумма:</label>
                    <input
                        type="number"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="Введите сумму"
                    />
                </div>
                <div className="form-group">
                    <label>Описание:</label>
                    <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
                </div>
                <button type="submit">Совершить операцию</button>
            </form>
        </div>
    );
};

export default OperationForm;

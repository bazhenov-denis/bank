import "./Transfer.css"
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../store/store.ts";


import React, {useState} from "react";
import {transferOperation} from "../../../store/OperationSlice.ts";
import {fetchAccounts} from "../../../store/AccountSlice.ts";


export const Transfer: React.FC = () => {
    const [modal, setModal] = useState<boolean>(false)
    const dispatch = useDispatch<AppDispatch>();
    const [selectedAccountFrom, setSelectedAccountFrom] = useState<number | "">("");
    const [selectedAccountWhere, setSelectedAccountWhere] = useState<number | "">("");

    const [amount, setAmount] = useState<string>("");
    const accounts = useSelector((state: RootState) => state.accounts.accounts);



    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedAccountFrom || !selectedAccountWhere) {
            alert("Выберите счёт и категорию");
            return;
        }

        const numericAmount = parseFloat(amount);
        if (isNaN(numericAmount) || numericAmount <= 0) {
            alert("Введите сумму больше нуля");
            return;
        }


        if (!selectedAccountFrom || !selectedAccountWhere) {
            alert("счёт не найден");
            return;
        }

        console.log(selectedAccountFrom);
        dispatch(
            transferOperation({
                fromAccountId: selectedAccountFrom,
                toAccountId: selectedAccountWhere,
                amount: numericAmount,
            })
        );
        await new Promise(resolve => setTimeout(resolve, 200)); // Задержка перед обновлением
        await dispatch(fetchAccounts()); // Обновляем счета после любой операции

        setSelectedAccountFrom("");
        setSelectedAccountWhere("");
        setAmount("");
    };


    return (<div className="transfer-container">
        <h2 className="transfer-header">Переводы</h2>
        <div className="transfer-body">
            <button onClick={() =>setModal(true)} className="transfer-button">перевести</button>
        </div>
        {modal && (
            <div className="modal-overlay">
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <button className="modal-close" onClick={() => setModal(false)}>
                        ✖
                    </button>
                    <div className="transfer-form-container">
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Откуда:</label>
                                <select value={selectedAccountFrom}
                                        onChange={(e) => setSelectedAccountFrom(Number(e.target.value))}>
                                    <option value="" disabled>Выберите счёт</option>
                                    {accounts.map((account) => (
                                        <option key={account.id} value={account.id}>
                                            {account.name} — Баланс: {account.balance}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Куда:</label>
                                <select value={selectedAccountWhere}
                                        onChange={(e) => setSelectedAccountWhere(Number(e.target.value))}>
                                    <option value="" disabled>Выберите счёт</option>
                                    {accounts.map((account) => (
                                        <option key={account.id} value={account.id}>
                                            {account.name} — Баланс: {account.balance}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Сколько:</label>
                                <input
                                    type="number"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    placeholder="Введите сумму"
                                />
                            </div>
                            <button type="submit">Совершить операцию</button>
                        </form>
                    </div>
                </div>
            </div>
        )}
    </div>)
}
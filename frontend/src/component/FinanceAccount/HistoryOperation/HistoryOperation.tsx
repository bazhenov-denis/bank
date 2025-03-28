import { useState } from "react";
import { getUserOperations } from "../../../api/api.ts"; // путь к вашему api
import "./HistoryOperetion.css";

export const HistoryOperation = () => {
    const [modal, setModal] = useState<boolean>(false);
    const [operations, setOperations] = useState<any[]>([]); // типизируйте под свою структуру

    // Функция для загрузки операций
    const fetchOperations = async () => {
        try {
            const data = await getUserOperations();
            setOperations(data);
        } catch (error) {
            console.error("Ошибка при получении операций пользователя:", error);
        }
    };

    // Открываем модалку и загружаем данные
    const openModal = () => {
        setModal(true);
        fetchOperations();
    };

    return (
        <>
            <div className="history-container">
                <h2 className="transfer-header">История операций</h2>
                <div className="transfer-body">
                    <button onClick={openModal} className="transfer-button">
                        Открыть историю
                    </button>
                </div>
                {modal && (
                    <div className="modal-overlay" onClick={() => setModal(false)}>
                        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                            <button className="modal-close" onClick={() => setModal(false)}>
                                ✖
                            </button>
                            <div className="history-operation">
                                {/* Пример отображения в виде таблицы */}
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Сумма</th>
                                        <th>Дата</th>
                                        <th>Описание</th>
                                        <th>Тип</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {operations.map((op) => (
                                        <tr key={op.id}>
                                            <td>{op.amount}</td>
                                            <td>{op.date}</td>
                                            <td>{op.description}</td>
                                            <td>{op.type}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </>
    );
};

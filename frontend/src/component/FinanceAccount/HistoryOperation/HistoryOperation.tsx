import {useState} from "react";

import "./HistoryOperetion.css"

export const HistoryOperation = () => {

    const [modal, setModal] = useState<boolean>(false)

    return (<>
        <div className="history-container">
            <h2 className="transfer-header">История операций</h2>
            <div className="transfer-body">
                <button onClick={() => setModal(true)} className="transfer-button">Открыть историю</button>
            </div>
            {modal && (
                <div className="modal-overlay">
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="modal-close" onClick={() => setModal(false)}>
                            ✖
                        </button>
                        <div className="history-operation">

                        </div>
                    </div>
                </div>
            )}
        </div>
    </>)
}
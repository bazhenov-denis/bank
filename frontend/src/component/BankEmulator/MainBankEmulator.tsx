import "./Bank.css"
import OperationForm from "./Operation/OperationForm.tsx";



export const MainBankEmulator = () => {
    return (
        <div>
            <div className="Bank">
                <h2>Эмулятор банка</h2>
            </div>
            <OperationForm></OperationForm>
        </div>
    )
}
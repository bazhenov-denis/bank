import {MainBankEmulator} from "./BankEmulator/MainBankEmulator.tsx";
import {MainFinanceAccount} from "./FinanceAccount/MainFinanceAccount.tsx";
import "./MainGrid.css"

export const MainGrid = () => {
    return (
        <div className="main-page-container">
            <MainFinanceAccount></MainFinanceAccount>
            <MainBankEmulator></MainBankEmulator>
        </div>
    )
}
import AccountList from "./AccountList/AccountList.tsx";
import "./Finance.css"
import {Header} from "./Header/Header.tsx";
import {Transfer} from "./Transfer/Transfer.tsx";
import {HistoryOperation} from "./HistoryOperation/HistoryOperation.tsx";

export const MainFinanceAccount = () => {
    return (
        <div className="finance-account">
            <Header></Header>
            <AccountList></AccountList>
            <Transfer></Transfer>
            <HistoryOperation></HistoryOperation>
        </div>
    )
}
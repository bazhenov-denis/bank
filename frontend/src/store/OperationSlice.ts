// operationsSlice.ts
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {createOperation, transfer} from "../api/api";
import {fetchAccounts} from "./AccountSlice.ts";
import { BankAccount } from "../component/types.ts"; // Подключаем тип


// Описание типа операции
interface OperationData {
    type: "INCOME" | "EXPENSE" | "TRANSFER";
    amount: number;
    description: string;
    date: string;
    bankAccount: BankAccount; // Теперь здесь не id, а сам объект
    category_id: number;
}

// Thunk: Создание операции
export const addOperation = createAsyncThunk(
    "operations/addOperation",
    async (operationData: OperationData, { dispatch }) => {
        await createOperation(operationData);
        dispatch(fetchAccounts()); // Обновляем счета после операции
    }
);

interface TransferData {
    fromAccountId: number;
    toAccountId: number;
    amount: number;
}

export const transferOperation = createAsyncThunk(
    "operations/transferOperation",
    async (transferData: TransferData, { dispatch }) => {
        await transfer(transferData);
        dispatch(fetchAccounts());

    }
);

const operationsSlice = createSlice({
    name: "operations",
    initialState: {},
    reducers: {},
});

export default operationsSlice.reducer;

// src/store/accountsSlice.ts
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { getAllAccounts, createAccount, deleteAccount } from "../api/api";

interface BankAccount {
    id: number;
    name: string;
    balance: number;
}

interface AccountsState {
    accounts: BankAccount[];
    status: "idle" | "loading" | "failed";
}

const initialState: AccountsState = {
    accounts: [],
    status: "idle",
};

// Асинхронное получение счетов
export const fetchAccounts = createAsyncThunk("accounts/fetchAccounts", async () => {
    return await getAllAccounts();
});

// Создание нового счета
export const addAccount = createAsyncThunk("accounts/addAccount", async (name: string, { dispatch }) => {
    await createAccount({ name, balance: 0 });
    dispatch(fetchAccounts()); // Обновляем список после создания
});

// Удаление счета
export const removeAccount = createAsyncThunk("accounts/removeAccount", async (id: number, { dispatch }) => {
    await deleteAccount(id);
    dispatch(fetchAccounts()); // Обновляем список после удаления
});

const accountsSlice = createSlice({
    name: "accounts",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchAccounts.pending, (state) => {
                state.status = "loading";
            })
            .addCase(fetchAccounts.fulfilled, (state, action) => {
                state.status = "idle";
                state.accounts = action.payload;
            })
            .addCase(fetchAccounts.rejected, (state) => {
                state.status = "failed";
            });
    },
});

export default accountsSlice.reducer;

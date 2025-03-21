import axios from 'axios';
import { BankAccount } from "../component/types.ts";

const api = axios.create({
    baseURL: "http://localhost:8080", // Замените на URL вашего бэкенда
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // замените на адрес вашего backend
    headers: {
        'Content-Type': 'application/json',
    },
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Bank Accounts
export const getAllAccounts = async (): Promise<BankAccount[]> => {
    const response = await apiClient.get<BankAccount[]>('/accounts');
    return response.data;
};

export const getAllCategories = async () => {
    const token = localStorage.getItem("token");
    const response = await fetch("http://localhost:8080/api/categories", {
        headers: {
            "Content-Type": "application/json",
            "Authorization": token ? `Bearer ${token}` : ""
        }
    });
    if (!response.ok) {
        throw new Error("Ошибка получения категорий");
    }
    return response.json();
};

export const createOperation = async (operationData: any) => {
    const token = localStorage.getItem("token");
    const response = await fetch("http://localhost:8080/api/operations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token ? `Bearer ${token}` : ""
        },
        body: JSON.stringify(operationData)
    });
    if (!response.ok) {
        throw new Error("Ошибка создания операции");
    }
    return response.json();
};

export const getAccountById = async (id: number): Promise<BankAccount> => {
    const response = await apiClient.get<BankAccount>(`/accounts/${id}`);
    return response.data;
};

export const createAccount = async (account: Partial<BankAccount>): Promise<BankAccount> => {
    const response = await apiClient.post<BankAccount>('/accounts', account);
    return response.data;
};

export const updateAccount = async (id: number, account: BankAccount): Promise<BankAccount> => {
    const response = await apiClient.put<BankAccount>(`/accounts/${id}`, account);
    return response.data;
};

export const deleteAccount = async (id: number): Promise<void> => {
    await apiClient.delete(`/accounts/${id}`);
};

export const transfer = async (transferData: { fromAccountId: number; toAccountId: number; amount: number }) => {
    const token = localStorage.getItem("token");
    const response = await fetch("http://localhost:8080/api/operations/transfer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token ? `Bearer ${token}` : ""
        },
        body: JSON.stringify({
            fromAccountId: transferData.fromAccountId,
            toAccountId: transferData.toAccountId,
            amount: transferData.amount,
        })
    });

    if (!response.ok) {
        throw new Error("Ошибка перевода средств");
    }

    return response.json();
};

export default api;

// types.ts
export interface BankAccount {
    id: number;
    name: string;
    balance: number;
}

export type CategoryType = 'INCOME' | 'EXPENSE' | 'TRANSFER';

export interface Category {
    id: number;
    type: CategoryType;
    name: string;
}

export type OperationType = 'INCOME' | 'EXPENSE' | 'TRANSFER';

export interface Operation {
    id: number;
    type: OperationType;
    bankAccount: BankAccount;
    amount: number;
    date: string; // можно использовать ISO-формат
    description?: string;
    category: Category;
}

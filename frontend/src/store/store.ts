// store.ts
import { configureStore } from "@reduxjs/toolkit";
import accountsReducer from "./AccountSlice.ts";
import operationsReducer from "./OperationSlice.ts";
import authReducer from "./AuthSlice.ts";

export const store = configureStore({
    reducer: {
        accounts: accountsReducer,
        operations: operationsReducer,
        auth: authReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

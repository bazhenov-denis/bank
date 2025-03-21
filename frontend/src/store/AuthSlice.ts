// src/store/authSlice.ts
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import api from "../api/api.ts";

interface AuthState {
    token: string | null;
    user: any | null;
    status: "idle" | "loading" | "failed";
}

const initialState: AuthState = {
    token: null,
    user: null,
    status: "idle",
};

export const login = createAsyncThunk(
    "auth/login",
    async ({ username, password }: { username: string; password: string }) => {
        const response = await api.post("/api/auth/login", { username, password });
        // Ожидается, что бэкенд вернёт { token, user }
        return response.data;
    }
);

export const register = createAsyncThunk(
    "auth/register",
    async ({ username, password }: { username: string; password: string }) => {
        const response = await api.post("/api/auth/register", { username, password });
        return response.data;
    }
);

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        logout(state) {
            state.token = null;
            state.user = null;
            localStorage.removeItem("token");
        },
        setCredentials(state, action) {
            state.token = action.payload.token;
            state.user = action.payload.user;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(login.pending, (state) => {
                state.status = "loading";
            })
            .addCase(login.fulfilled, (state, action) => {
                state.status = "idle";
                state.token = action.payload.token;
                state.user = action.payload.user;
                localStorage.setItem("token", action.payload.token);
            })
            .addCase(login.rejected, (state) => {
                state.status = "failed";
            })
            .addCase(register.pending, (state) => {
                state.status = "loading";
            })
            .addCase(register.fulfilled, (state) => {
                state.status = "idle";
            })
            .addCase(register.rejected, (state) => {
                state.status = "failed";
            });
    },
});

export const { logout, setCredentials } = authSlice.actions;
export default authSlice.reducer;

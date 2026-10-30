import type {AuthResponse} from "../api/types/auth.ts";
import {create} from "zustand/react";
import {clearTokens, saveTokens} from "./token-storage.ts";

type SessionStore = {
    tokens: AuthResponse | null;
    setSession: (tokens: AuthResponse) => void;
    clearSession: () => void;
}

export const useSessionStore = create<SessionStore>()((set) => ({
        tokens: null,

        setSession: (newTokens: AuthResponse) => {
            saveTokens(newTokens);
            set({
                tokens: newTokens,
            });
        },
        clearSession: () => {
            clearTokens()
            set({
                tokens: null,
            })
        }
    }
));
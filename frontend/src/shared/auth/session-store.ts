import type {AuthResponse} from "../api/types/auth.ts";
import {create} from "zustand/react";
import {clearTokens, readTokens, saveTokens} from "./token-storage.ts";
import {refreshSession} from "../api/auth-api.ts";

type SessionStore = {
    tokens: AuthResponse | null;
    status: SessionStatus;
    setSession: (tokens: AuthResponse) => void;
    clearSession: () => void;
    restoreSession: () => Promise<void>;

}
type SessionStatus = 'restoring' | 'authenticated' | 'anonymous';

export const useSessionStore = create<SessionStore>()((set, get) => ({
        tokens: null,
        status: 'restoring',

        setSession: (newTokens: AuthResponse) => {
            saveTokens(newTokens);
            set({
                tokens: newTokens,
                status: 'authenticated',
            });

        },
        clearSession: () => {
            clearTokens()
            set({
                tokens: null,
                status: 'anonymous'
            })
        },
        restoreSession: async () => {
            try {
                const tokens = readTokens();

                if (!tokens) {
                    get().clearSession()
                    return;
                } else {

                    const authResponse = await refreshSession({refreshToken: tokens.refreshToken});
                    get().setSession(authResponse);

                }
            } catch {
                get().clearSession();
            }

        }

    }
));
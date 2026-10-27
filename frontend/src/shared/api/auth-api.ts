import type {AuthRequest, AuthResponse, RefreshTokenRequest, UserMeResponse} from "./types/auth.ts";
import {apiFetch} from "./http.ts";

export async function login(request: AuthRequest): Promise<AuthResponse> {
    const response = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify(request),
    });

    return response.json();
}

export async function refreshSession(request: RefreshTokenRequest): Promise<AuthResponse> {
    const response = await apiFetch("/auth/refresh", {
        method: "POST",
        body: JSON.stringify(request),
    })

    return response.json();
}

export async function getMe(accessToken: string): Promise<UserMeResponse> {
    const response = await apiFetch("/auth/me", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${accessToken}`,
        }
    })

    return response.json();
}
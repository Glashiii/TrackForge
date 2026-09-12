import axios, { type AxiosError, type AxiosResponse, type InternalAxiosRequestConfig } from "axios";
import {env} from "../config/env.ts"
import {useAuthStore} from "../store/auth-store.ts";

type RefreshTokenResponse = {
    accessToken: string
    refreshToken: string
}

type RetryableRequestConfig = InternalAxiosRequestConfig & {
    _retry?: boolean
}

export const authHttp = axios.create({
    baseURL: env.authApiUrl,
    headers: {
        'Content-Type': 'application/json',
    },
})

export const coreHttp = axios.create({
    baseURL: env.coreApiUrl,
    headers: {
        'Content-Type': 'application/json',
    },
})

coreHttp.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const {accessToken} = useAuthStore.getState()
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`
    }
    return config
})

coreHttp.interceptors.response.use(
    (response: AxiosResponse) => response,
    async (error: AxiosError) => {
        const originalRequest = error.config as RetryableRequestConfig | undefined

        if (!originalRequest || error.response?.status !== 401 || originalRequest._retry) {
            throw error
        }

        originalRequest._retry = true

        const refreshToken = useAuthStore.getState().refreshToken

        if (!refreshToken) {
            useAuthStore.getState().clearSession()
            throw error
        }

        const response = await authHttp.post<RefreshTokenResponse>('/auth/refresh', {
            refreshToken,
        })

        useAuthStore.getState().setTokens(response.data)

        originalRequest.headers['Authorization'] = `Bearer ${response.data.accessToken}`

        return coreHttp(originalRequest)
    }
)

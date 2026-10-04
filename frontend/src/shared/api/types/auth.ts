export type AuthRequest = {
    email: string,
    password: string
}

export type AuthResponse = {
    accessToken: string,
    refreshToken: string,
}

export type RefreshTokenRequest = {
    refreshToken: string
}

export type UserMeResponse = {
    id: number,
    email: string,
    username: string,
    roles: string
}
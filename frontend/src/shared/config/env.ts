export const env = {
    authApiUrl: import.meta.env.VITE_AUTH_API_URL,
    coreApiUrl: import.meta.env.VITE_CORE_API_URL,
}

if (!env.authApiUrl) {
    throw new Error('VITE_AUTH_API_URL is not defined')
}

if (!env.coreApiUrl) {
    throw new Error('VITE_CORE_API_URL is not defined')
}
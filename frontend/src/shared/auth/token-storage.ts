import type {AuthResponse} from "../api/types/auth.ts";

export function saveTokens(tokens: AuthResponse): void {
    sessionStorage.setItem('taskforge.tokens', JSON.stringify(tokens));
}

export function clearTokens(): void {
    sessionStorage.removeItem('taskforge.tokens');
}

export function readTokens(): AuthResponse | null {

    const tokens = sessionStorage.getItem('taskforge.tokens');

    if (tokens === null) {
        return null;
    }

    try {


        const tokensJson: unknown = JSON.parse(tokens);
        if (
            tokensJson !== null
            && typeof tokensJson === 'object'

            && "accessToken" in tokensJson
            && typeof tokensJson.accessToken === "string"
            && tokensJson.accessToken.length > 0

            && "refreshToken" in tokensJson
            && typeof tokensJson.refreshToken === "string"
            && tokensJson.refreshToken.length > 0

        ) {
            return {
                accessToken: tokensJson.accessToken,
                refreshToken: tokensJson.refreshToken,
            }
        }
    }
    catch {
        // Corrupted JSON
    }

    clearTokens()
    return null;
}
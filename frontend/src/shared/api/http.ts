export async function apiFetch(path: string, options: RequestInit = {}) {
    const headers = new Headers(options?.headers);
    if (options.body && !headers.has("Content-Type")) {
        headers.append("Content-Type", "application/json");
    }

    const response = await fetch("/api" + path, {...options, headers});

    if (!response.ok) {
        const rawText = await response.text();
        let message = rawText || `HTTP ${response.status}`;
        try {
            const data: unknown = JSON.parse(rawText)
            if (typeof data === "object" && data !== null) {
                if ("detail" in data && typeof data.detail === "string") {
                    message = data.detail;
                } else if ("message" in data && typeof data.message === "string") {
                    message = data.message;
                } else if ("title" in data && typeof data.title === "string") {
                    message = data.title;
                }
            }
        } catch {
            // Body in not JSON
        }
        throw new ApiError(response.status, message);
    }
    return response;

}

export class ApiError extends Error {
    status: number;

    constructor(status: number, message: string) {
        super(message);
        this.status = status;
    }
}

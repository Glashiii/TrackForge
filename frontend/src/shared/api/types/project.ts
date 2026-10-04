export type ProjectResponse = {
    id: number,
    ownerId: number,
    name: string,
    key: string,
    description: string | null,
    createdAt:  string,
    updatedAt: string
}

export type ProjectCreateRequest = {
    name: string,
    key: string,
    description?: string | null,
}

export type ProjectUpdateRequest = {
    name?: string | null,
    description?: string | null
}
export type BoardColumnResponse = {
    id: number,
    boardId: number,
    projectId: number,
    position: number,
    name: string,
    createdAt: string,
    updatedAt: string
}

export type BoardResponse = {
    id: number,
    projectId: number,
    createdAt: string,
    updatedAt: string
    columns: BoardColumnResponse[]
}
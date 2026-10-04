export type IssueType = "TASK" | "BUG" | "STORY"

export type IssuePriority = "LOW" | "MEDIUM" | "HIGH"

export type IssueCreateRequest = {
    issueType: IssueType,
    title: string,
    priority: IssuePriority,
    description?: string | null,
    assigneeId?: number | null,
}

export type IssueUpdateRequest = {
    title?: string | null,
    priority?: IssuePriority | null,
    description?: string | null,
    assigneeId?: number | null,
}

export type IssueMoveRequest = {
    columnId: number,
    position: number
}

export type IssueResponse = {
    id: number,
    projectId: number,
    reporterId: number,
    position: number,
    columnId: number,
    issueNumber: number,
    title: string,
    createdAt: string,
    updatedAt:  string,
    description: string | null
    assigneeId: number | null,
    type: IssueType,
    priority: IssuePriority
}
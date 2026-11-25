import type {ProjectCreateRequest, ProjectResponse, ProjectUpdateRequest} from "./types/project.ts";
import {apiFetch} from "./http.ts";

export async function getProjects(accessToken: string): Promise<ProjectResponse[]> {
    const response = await apiFetch("/projects", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${accessToken}`,
        }
    });


    return response.json();
}

export async function createProject(request: ProjectCreateRequest, accessToken: string): Promise<ProjectResponse> {
    const response = await apiFetch("/projects", {
        method: "POST",
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(request),
    });

    return response.json();
}

export async function updateProject(projectId: number, request: ProjectUpdateRequest, accessToken: string): Promise<ProjectResponse> {
    const response = await apiFetch(`/projects/${projectId}`, {
        method: "PATCH",
        headers: {
            authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(request),
    });

    return response.json();
}

export async function getProjectById(projectId: number, accessToken: string): Promise<ProjectResponse> {
    const response = await apiFetch(`/projects/${projectId}`, {
        method: "GET",
        headers: {
            authorization: `Bearer ${accessToken}`,
        }
    })

    return response.json();
}
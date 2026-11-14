import type {ProjectCreateRequest, ProjectResponse} from "./types/project.ts";
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
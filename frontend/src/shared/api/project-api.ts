import type {ProjectResponse} from "./types/project.ts";
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
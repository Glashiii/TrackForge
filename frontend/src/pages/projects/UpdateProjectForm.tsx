import type {ProjectResponse, ProjectUpdateRequest} from "../../shared/api/types/project.ts";
import {useMutation} from "@tanstack/react-query";
import {useState} from "react";
import {useSessionStore} from "../../shared/auth/session-store.ts";
import {updateProject} from "../../shared/api/project-api.ts";
import {queryClient} from "../../shared/api/query-client.ts";

type UpdateProjectFormProps = {
    project: ProjectResponse
    onUpdated: () => void;
}

export default function UpdateProjectForm({project, onUpdated}: UpdateProjectFormProps) {

    const [projectName, setProjectName] = useState<string>(project.name);
    const [projectDescription, setProjectDescription] = useState<string>(project.description ? project.description : "");
    const [validationError, setValidationError] = useState<string | null>(null);
    const accessToken = useSessionStore(
        (state) => state.tokens?.accessToken,
    );

    const updateMutation = useMutation({
            mutationFn: (request: ProjectUpdateRequest) => {
                if (!accessToken) {
                    throw new Error('No access token');
                }

                return updateProject(project.id ,request, accessToken)
            },
            onSuccess: async () => {
                await queryClient.invalidateQueries({
                    queryKey: ['projects'],
                });

                setProjectName("");
                setProjectDescription("");

                onUpdated();
            }
        }
    )

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (updateMutation.isPending) return

        setValidationError(null);
        updateMutation.reset();

        try {
            if (projectName.trim() === "") {
                setValidationError("Project name is required");
                return;
            }


            updateMutation.mutate({
                name: projectName,
                description: projectDescription
            })


        } catch (error) {
            if (error instanceof Error) {
                setValidationError(error.message);
            } else {
                setValidationError("An unexpected error occurred")
            }
        }

    }

    return (
        <div>
            <form className={"flex flex-col"} onSubmit={handleSubmit}>
                <input id="projectName" type="text" value={projectName} maxLength={100} required placeholder={"Name"}
                       onChange={(e) => setProjectName(e.target.value)}/>
                <input className="opacity-25" id="projectKey" type="text" value={project.key} disabled={true} required placeholder={"Key"} title={"Project key cannot be changed"}/>
                <input id="projectDescription" type="text" value={projectDescription} maxLength={255} placeholder={"Description"}
                       onChange={(e) => setProjectDescription(e.target.value)}/>

                <button type="submit" disabled={updateMutation.isPending}>Confirm changes</button>
            </form>

            {validationError && (
                <p role={"alert"}>{validationError}</p>
            )}

            {updateMutation.isError && (
                <p role="alert">{updateMutation.error.message}</p>
            )}
        </div>
    )

}
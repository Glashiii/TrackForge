import {type FormEvent, useState} from "react";
import {useMutation} from "@tanstack/react-query";
import {createProject} from "../../shared/api/project-api.ts";
import {useSessionStore} from "../../shared/auth/session-store.ts";
import type {ProjectCreateRequest} from "../../shared/api/types/project.ts";
import {queryClient} from "../../shared/api/query-client.ts";

type CreateProjectFormProps = {
    onCreated: () => void;
};

export default function CreateProjectForm({onCreated}: CreateProjectFormProps) {

    const [projectName, setProjectName] = useState<string>("");
    const [projectKey, setProjectKey] = useState<string>("");
    const [projectDescription, setProjectDescription] = useState<string>("");
    const [validationError, setValidationError] = useState<string | null>(null);
    const accessToken = useSessionStore(
        (state) => state.tokens?.accessToken,
    );

    const createMutation = useMutation({
        mutationFn: (request: ProjectCreateRequest) => {
            if (!accessToken) {
                throw new Error('No access token');
            }

            return createProject(request, accessToken)
        },
        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ['projects'],
            });

            setProjectName("");
            setProjectKey("")
            setProjectDescription("");

            onCreated();
        },


    })

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (createMutation.isPending) return

        setValidationError(null);
        createMutation.reset();

        try {
            if (projectName.trim() === "") {
                setValidationError("Project name is required");
                return;
            }

            if (!projectKey.match(/^[A-Z][A-Z0-9]{1,9}$/)) {
                setValidationError("Project key not correct. Example DEMO1 ");
                return;
            }

            createMutation.mutate({
                name: projectName,
                key: projectKey,
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
                <input id="projectKey" type="text" value={projectKey} required placeholder={"Key"}
                       onChange={(e) => setProjectKey(e.target.value)}/>
                <input id="projectDescription" type="text" value={projectDescription} maxLength={255} placeholder={"Description"}
                       onChange={(e) => setProjectDescription(e.target.value)}/>

                <button type="submit" disabled={createMutation.isPending}>Create</button>
            </form>

            {validationError && (
                <p role={"alert"}>{validationError}</p>
            )}

            {createMutation.isError && (
                <p role="alert">{createMutation.error.message}</p>
            )}
        </div>
    )

}
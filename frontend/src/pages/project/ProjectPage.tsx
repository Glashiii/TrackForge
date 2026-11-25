import {Link, useNavigate, useParams} from "react-router";
import {useMutation, useQuery} from "@tanstack/react-query";
import {deleteProject, getProjectById} from "../../shared/api/project-api.ts";
import {useSessionStore} from "../../shared/auth/session-store.ts";
import {ApiError} from "../../shared/api/http.ts";
import {queryClient} from "../../shared/api/query-client.ts";

export default function ProjectPage() {

    const navigate = useNavigate();
    const status = useSessionStore((state) => state.status)
    const accessToken = useSessionStore((state) => state.tokens?.accessToken)
    const projectId = useParams().projectId;
    const projectNumberId = Number(projectId);
    const isValidProjectId = (projectNumberId > 0 && Number.isSafeInteger(projectNumberId));

    const projectQuery = useQuery({
        queryKey: ["project", projectNumberId],
        queryFn: () => {
            if (!accessToken) {
                throw new Error("No token provided");
            }

            if (!isValidProjectId) {
                throw new Error("Invalid project id");
            }

            return getProjectById(projectNumberId, accessToken)
        },
        enabled: Boolean(status === "authenticated" && accessToken && isValidProjectId),
    });

    const error = projectQuery.error;

    const deleteMutation = useMutation({
            mutationFn: () => {
                if (!accessToken) {
                    throw new Error('No access token');
                }

                return deleteProject(projectNumberId, accessToken);
            },
            onSuccess: async () => {
                await queryClient.invalidateQueries({
                    queryKey: ['projects'],
                });

                navigate(`/projects`);
            }
        }
    )


    const handleDeleteProject = () => {
        if (deleteMutation.isPending) return
        deleteMutation.mutate();
    }

    return (
        <div>
            <h1>Project page</h1>

            <div className="flex flex-col">

                {!isValidProjectId ? (
                        <p>Not valid project id</p>
                    ) :
                    projectQuery.isLoading ? (
                        <p>Project is loading...</p>
                    ) : projectQuery.isError ? (
                            (error instanceof ApiError && error.status === 404) ?
                                (
                                    <p>Project not found</p>
                                ) :
                                (
                                    <p>{projectQuery.error.message}</p>
                                )

                        ) :
                        (
                            <div className="flex flex-col">
                                <p>Project name: {projectQuery.data?.name}</p>
                                <p>Project
                                    description: {projectQuery.data?.description ? projectQuery.data?.description : "No description for project"} </p>
                                <p>Project key: {projectQuery.data?.key}</p>
                                <button disabled={deleteMutation.isPending} onClick={handleDeleteProject}>Delete</button>
                            </div>

                        )}
            </div>
            <Link to={"/projects"}>Back</Link>
        </div>
    )
}
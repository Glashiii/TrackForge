import {Link} from "react-router";
import {useSessionStore} from "../../shared/auth/session-store.ts";
import {useQuery} from "@tanstack/react-query";
import {getProjects} from "../../shared/api/project-api.ts";

export default function ProjectsPage() {

    const status = useSessionStore((state) => state.status)
    const accessToken = useSessionStore((state) => state.tokens?.accessToken)

    const projectsQuery = useQuery({
        queryKey: ["projects"],
        queryFn: () => {
            if (!accessToken) {
                throw new Error("No token provided");
            }
            return getProjects(accessToken)
        },
        enabled: Boolean(status === "authenticated" && accessToken),
    });

    return (
        <>
            <h1>Projects page</h1>

            {projectsQuery.isPending ? (
                <p>Projects are loading...</p>
            ) : projectsQuery.isError ? (
                <p>{projectsQuery.error.message}</p>
            ) : projectsQuery.data.length === 0 ? (
                <p>No projects yet</p>
            ) : (
                projectsQuery.data.map((project) => (
                    <div key={project.id}>
                        <h3>{project.name}</h3>
                        <Link to={`/projects/${project.id}`}>Go to project {project.name}</Link>
                    </div>
                ))
            )}

        </>
    )
}
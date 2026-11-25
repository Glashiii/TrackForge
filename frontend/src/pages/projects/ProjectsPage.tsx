
import {useSessionStore} from "../../shared/auth/session-store.ts";
import {useQuery} from "@tanstack/react-query";
import {getProjects} from "../../shared/api/project-api.ts";
import CreateProjectForm from "./CreateProjectForm.tsx";
import {useState} from "react";
import Modal from "../../shared/ui/Modal.tsx";
import UpdateProjectForm from "./UpdateProjectForm.tsx";
import ProjectCard from "../../shared/ui/ProjectCard.tsx";

export default function ProjectsPage() {

    const status = useSessionStore((state) => state.status)
    const accessToken = useSessionStore((state) => state.tokens?.accessToken)
    const [isCreateOpen, setIsCreateOpen] = useState(false);
    const [editingProjectId, setEditingProjectId] = useState<number | null>(null);

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

    const editingProject = projectsQuery.data?.find(
        (project) => project.id === editingProjectId,
    )


    return (
        <div className="flex-col">
            <h1>Projects</h1>


            <div className="grid grid-cols-1 md:grid-cols-2">
                {projectsQuery.isPending ? (
                    <p>Projects are loading...</p>
                ) : projectsQuery.isError ? (
                    <p>{projectsQuery.error.message}</p>
                ) : projectsQuery.data.length === 0 ? (
                    <p>No projects yet</p>
                ) : (
                    projectsQuery.data.map((project) => (
                        <ProjectCard project={project} setId={() => setEditingProjectId(project.id)}/>
                    ))
                )}
            </div>


            <button type="button" onClick={() => setIsCreateOpen(true)}>
                Create project
            </button>

            <Modal
                open={isCreateOpen}
                onClose={() => setIsCreateOpen(false)}
                title="Create project"
            >
                <CreateProjectForm onCreated={() => setIsCreateOpen(false)}/>
            </Modal>


            {editingProject && (
                <Modal
                    open={editingProjectId !== null}
                    onClose={() => setEditingProjectId(null)}
                    title={"Update project"}>
                    <UpdateProjectForm
                        // key is not mandatory, because 'open' is unmounting form
                        key={editingProject?.id}
                        project={editingProject}
                        onUpdated={() => setEditingProjectId(null)}/>
                </Modal>
            )}


        </div>
    )
}
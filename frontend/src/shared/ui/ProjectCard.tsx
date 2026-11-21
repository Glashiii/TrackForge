import type {ProjectResponse} from "../api/types/project.ts";
import {Link} from "react-router";

type ProjectCardProps = {
    project: ProjectResponse;
    setId: (id: number) => void;
}

export default function ProjectCard(projectCardProps: ProjectCardProps ) {

    return (
        <div key={projectCardProps.project.id} className="border-2">
            <h3>{projectCardProps.project.name}</h3>
            <Link to={`/projects/${projectCardProps.project.id}`}>Go to project {projectCardProps.project.name}</Link>
            <button onClick={() => {projectCardProps.setId(projectCardProps.project.id)}}>Update</button>
        </div>
    )
}
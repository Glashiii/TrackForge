import {Link, useParams} from "react-router";

export default function ProjectPage() {

    const projectId = useParams().projectId;

    return (
        <>
            <h1>Project page</h1>
            <p>project id: {projectId}</p>
            <Link to={"/projects"}>Back</Link>
        </>
    )
}
import {Link} from "react-router";

export default function NotFoundPage() {
    return (
        <>
            <h1>Not found</h1>
            <Link to={"/projects"}>Back</Link>
        </>
    )
}
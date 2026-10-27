import {Link} from "react-router";


type LinkProps = {
    route: string,
    title: string
}

export default function LinkComponent({route, title}: LinkProps) {
    return (
        <Link className="p-2 hover:bg-gray-200 transition duration-200 ease-in-out " to={route}>{title}</Link>
    )
}
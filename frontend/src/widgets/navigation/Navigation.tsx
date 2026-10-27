
import LinkComponent from "./LinkComponent.tsx";

export default function Navigation() {

    return (
        <nav
            className="flex items-center justify-between px-4 py-4 bg-white border border-gray-200 rounded-lg">
            <LinkComponent route="/projects" title="Projects" />
            <LinkComponent route="/login" title="Login" />
        </nav>
    )
}
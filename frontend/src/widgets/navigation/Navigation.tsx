import LinkComponent from "./LinkComponent.tsx";
import {useSessionStore} from "../../shared/auth/session-store.ts";

export default function Navigation() {

    const status = useSessionStore((state) => state.status);
    const clearSession = useSessionStore((state) => state.clearSession);

    return (
        <nav
            className="flex items-center justify-between px-4 py-4 bg-white border border-gray-200 rounded-lg">
            
            {status === "authenticated" && (
                <>
                    <LinkComponent route="/projects" title="Projects"/>
                    <button onClick={() => clearSession()} type="button">Log out</button>
                </>
            )}
            {status === "anonymous" && (
                <>
                    <LinkComponent route="/login" title="Login"/>
                </>
            )}

        </nav>
    )
}
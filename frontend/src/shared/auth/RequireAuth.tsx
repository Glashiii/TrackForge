import {useSessionStore} from "./session-store.ts";
import {Navigate, Outlet} from "react-router";

export default function RequireAuth() {
    const status = useSessionStore((state) => state.status);

    return (
        <div>
            {status === 'restoring' && (
                <p>Waiting...</p>
            )}
            {status === "anonymous" && (
                <Navigate to="/login" replace/>
            )}
            {status === "authenticated" && (
                <Outlet/>
            )}
        </div>
    )
}
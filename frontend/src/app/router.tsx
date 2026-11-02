import {Navigate, Route, Routes} from "react-router";
import ProjectPage from "../pages/project/ProjectPage.tsx";
import ProjectsPage from "../pages/projects/ProjectsPage.tsx";
import LoginPage from "../pages/login/LoginPage.tsx";
import NotFoundPage from "../pages/not-found/NotFoundPage.tsx";
import RequireAuth from "../shared/auth/RequireAuth.tsx";


export function AppRouter() {

    return (
        <Routes>
            <Route path="/" element={<Navigate to={"/login"} replace={true} />} />
            <Route element={<RequireAuth />}>
                <Route path="/projects" element={<ProjectsPage />} />
                <Route path="/projects/:projectId" element={<ProjectPage />} />
            </Route>
            <Route path="/login" element={<LoginPage />} />
            <Route path="*" element={<NotFoundPage />} />
        </Routes>
    )

}
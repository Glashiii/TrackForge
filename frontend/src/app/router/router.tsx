import {createBrowserRouter, Navigate} from 'react-router-dom'
import {LoginPage} from "../../pages/login/login-page.tsx";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <Navigate to="/projects" replace/>,
    },
    {
        path: '/login',
        element: <LoginPage/>,
    },
    // {
    //     path: '/register',
    //     element: <RegisterPage/>,
    // },
    // {
    //     element: <ProtectedRoute/>,
    //     children: [
    //         {
    //             path: '/projects',
    //             element: <ProjectsPage/>,
    //         },
    //         {
    //             path: '/projects/:projectId/board',
    //             element: <BoardPage/>,
    //         },
    //     ]
    // }

])
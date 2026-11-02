import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import {BrowserRouter} from "react-router";
import {useSessionStore} from "./shared/auth/session-store.ts";
import {queryClient} from "./shared/api/query-client.ts";
import {QueryClientProvider} from "@tanstack/react-query";

void useSessionStore.getState().restoreSession()
createRoot(document.getElementById('root')!).render(
    <BrowserRouter>
        <StrictMode>
            <QueryClientProvider client={queryClient}>
                <App/>
            </QueryClientProvider>
        </StrictMode>
    </BrowserRouter>,
)

import './App.css'
import {AppRouter} from "./app/router.tsx";
import Navigation from "./widgets/navigation/Navigation.tsx";
import {useLocation} from "react-router";
import {useSessionStore} from "./shared/auth/session-store.ts";

function App() {

    const {pathname} = useLocation();
    const status = useSessionStore((state) => state.status)

    return (
        <div className="flex h-screen flex-col items-center justify-start bg-white">
            {status !== 'restoring' &&
                <div className="flex h-full flex-col items-center">
                    {pathname !== '/login' && <Navigation/>}

                    <AppRouter/>
                </div>
            }

            {status === 'restoring' &&
            <div className="flex h-full justify-center align-items-center">
                <p>Restoring...</p>
            </div>

            }

        </div>

    )
}

export default App

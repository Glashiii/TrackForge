import './App.css'
import {AppRouter} from "./app/router.tsx";
import Navigation from "./widgets/navigation/Navigation.tsx";
import {useLocation} from "react-router";

function App() {

    const {pathname} = useLocation();

    return (
        <div className="flex h-screen flex-col items-center justify-start bg-white">
            {pathname !== '/login' && <Navigation />}

          <AppRouter/>
        </div>

    )
}

export default App

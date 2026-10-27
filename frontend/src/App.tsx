import './App.css'
import {AppRouter} from "./app/router.tsx";
import Navigation from "./widgets/navigation/Navigation.tsx";

function App() {

    return (
        <div className="flex h-screen flex-col items-center justify-start bg-white">
          <Navigation />

          <AppRouter/>
        </div>

    )
}

export default App

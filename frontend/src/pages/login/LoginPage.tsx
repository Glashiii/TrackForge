import {login} from "../../shared/api/auth-api.ts"
import {type FormEvent, useState} from "react";
import {useSessionStore} from "../../shared/auth/session-store.ts";

export default function LoginPage() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const setSession = useSessionStore((state) => state.setSession)

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (isSubmitting) return;

        setIsSubmitting(true);
        setError(null);
        setIsLoggedIn(false);

        try {
            const response = await login({email, password});
            setSession(response);
            setIsLoggedIn(true);
            setPassword('');

        }
        catch (error) {
            if (error instanceof Error) {
                setError(error.message);
            }
            else {
                setError("An unexpected error occurred")
            }
        }
        finally {
            setIsSubmitting(false);
        }
    }

    return (
        <>
            <h1>Login page</h1>
            {/*create a submit function*/}
            <form onSubmit={handleSubmit}>
                <label htmlFor={"email"}>Email</label>
                <input id="email" type="email" onChange={(e) => setEmail(e.target.value)} value={email} required />
                <label htmlFor={"password"}>Password</label>
                <input id="password" type="password" onChange={(e) => setPassword(e.target.value)} value={password} required />
                <button type="submit" disabled={isSubmitting}>{isSubmitting ? "Logging in" : "Login"}</button>
            </form>



            {error && <p role="alert">{error}</p>}
            {isLoggedIn && <p>Logged In</p>}

        </>
    )
}
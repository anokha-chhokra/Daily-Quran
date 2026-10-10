import { useEffect, useState } from "react";
import { checkHealth } from "./api/health";

function App(){
    const [backendStatus, setBackendStatus] = useState('checking')

    useEffect(() => {
        checkHealth()
        .then(() => setBackendStatus('connected'))
        .catch(() => setBackendStatus('unreachable'))
    }, [])

    return (
        <div className="app">
            <p data-testid="backend-status">
                Backend: {backendStatus}
            </p>
        </div>
    )
}

export default App
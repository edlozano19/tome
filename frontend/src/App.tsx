import { useEffect, useState } from 'react';

function App() {
  const [status, setStatus] = useState<string>('loading...');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/actuator/health')
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data: { status: string }) => setStatus(data.status))
      .catch((err: Error) => setError(err.message));
  }, []);

  return (
    <main>
      <h1>Tome</h1>
      {error ? <p>Error: {error}</p> : <p>Backend Health: {status}</p>}
    </main>
  );
}

export default App;

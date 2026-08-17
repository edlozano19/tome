import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export function LibraryPage() {
  const { user } = useAuth();

  return (
    <main>
      <h1>Library</h1>
      <p>Signed in as {user?.username}</p>
      <p>Book list will live here in Phase 3.</p>
      <p>
        <Link to="/">Home</Link>
      </p>
    </main>
  );
}

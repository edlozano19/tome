import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export function HomePage() {
  const { user, logout } = useAuth();

  return (
    <main>
      <h1>Tome</h1>

      {user ? (
        <>
          <p>Signed in as {user.username}</p>
          <button type="button" onClick={logout}>
            Log out
          </button>
        </>
      ) : (
        <>
          <p>Not signed in</p>
          <p>
            <Link to="/login">Log in</Link>
            {' . '}
            <Link to="/register">Register</Link>
          </p>
        </>
      )}
    </main>
  );
}

import { Link } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

export const Layout = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated, logout } = useAuth();

  return (
    <>
      <header className="flex justify-between p-4 border-b">
        <Link to="/">EasyJobSpot</Link>

        {isAuthenticated ? (
          <button onClick={logout}>Logout</button>
        ) : (
          <div className="flex gap-4">
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </div>
        )}
      </header>

      <main>{children}</main>
    </>
  );
};

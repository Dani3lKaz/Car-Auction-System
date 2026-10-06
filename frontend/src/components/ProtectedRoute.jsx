import { Navigate } from 'react-router-dom';
import { useCurrentUser } from '../hooks/useCurrentUser.js';

function ProtectedRoute({ children, role }) {
    const { data: user, isLoading } = useCurrentUser();

    if (isLoading) return null;
    if (!user) return <Navigate to="/login" replace />;
    if (role && user.role !== role) return <Navigate to="/" replace />;

    return children;
}

export default ProtectedRoute;

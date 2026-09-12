import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
export function ProtectedRoute() { const { isAuthenticated } = useAuth(); const location = useLocation(); return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace state={{ from: location }} /> }
export function AdminRoute() { const { user } = useAuth(); return user?.isAdmin ? <Outlet /> : <Navigate to="/dashboard" replace /> }
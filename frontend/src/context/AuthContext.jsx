import { createContext, useContext, useState } from 'react'
import * as authService from '../services/authService'

const AuthContext = createContext(null)
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => { try { return JSON.parse(localStorage.getItem('leaveweb_user')) || null } catch { return null } })
  const login = async (credentials) => {
    const data = await authService.login(credentials.email, credentials.password)
    if (!data.token) throw new Error('Your account is waiting for administrator approval.')
    const nextUser = { ...data.user, isAdmin: data.user.role === 'ADMIN', name: data.user.fullName }
    setUser(nextUser); localStorage.setItem('leaveweb_user', JSON.stringify(nextUser)); return nextUser
  }
  const register = async (details) => {
    const response = await (await import('../services/api')).default.post('/auth/register', { employeeId: details.employeeId, fullName: details.name, email: details.email, password: details.password, phone: details.phone, department: details.department })
    return response.data.user
  }
  const logout = () => { authService.logout(); setUser(null) }
  return <AuthContext.Provider value={{ user, login, register, logout, isAuthenticated: Boolean(user), isAdmin: user?.role === 'ADMIN' }}>{children}</AuthContext.Provider>
}
export const useAuth = () => useContext(AuthContext)
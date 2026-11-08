import { createContext, useContext, useState } from 'react'
import { authApi } from '../api/services'

const AuthContext = createContext(null)

function readUser() {
  try {
    const u = localStorage.getItem('user')
    return u ? JSON.parse(u) : null
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readUser)

  const save = (res) => {
    const { token, ...u } = res
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(u))
    setUser(u)
    return u
  }

  const login = async (email, lozinka) => save(await authApi.login({ email, lozinka }))
  const register = async (data) => save(await authApi.register(data))
  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  const isAdmin = user?.uloga === 'ADMIN'

  return (
    <AuthContext.Provider value={{ user, isAdmin, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}

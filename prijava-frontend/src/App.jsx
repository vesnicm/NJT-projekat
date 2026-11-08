import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './auth/AuthContext'
import Layout from './components/Layout'
import Login from './pages/Login'
import Register from './pages/Register'
import MojePrijave from './pages/kandidat/MojePrijave'
import NovaPrijava from './pages/kandidat/NovaPrijava'
import PrijavaDetalji from './pages/kandidat/PrijavaDetalji'
import AdminPrijave from './pages/admin/Prijave'
import AdminPrijavaDetalji from './pages/admin/PrijavaDetalji'
import AdminProgrami from './pages/admin/Programi'
import AdminSpisak from './pages/admin/Spisak'


function Guard({ role, children }) {
  const { user, isAdmin } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  if (role === 'ADMIN' && !isAdmin) return <Navigate to="/moje-prijave" replace />
  if (role === 'KANDIDAT' && isAdmin) return <Navigate to="/admin/prijave" replace />
  return children
}

function Home() {
  const { user, isAdmin } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  return <Navigate to={isAdmin ? '/admin/prijave' : '/moje-prijave'} replace />
}

function PublicOnly({ children }) {
  const { user, isAdmin } = useAuth()
  if (user) return <Navigate to={isAdmin ? '/admin/prijave' : '/moje-prijave'} replace />
  return children
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<PublicOnly><Login /></PublicOnly>} />
          <Route path="/register" element={<PublicOnly><Register /></PublicOnly>} />

          <Route element={<Guard><Layout /></Guard>}>
            <Route path="/" element={<Home />} />

            
            <Route path="/moje-prijave" element={<Guard role="KANDIDAT"><MojePrijave /></Guard>} />
            <Route path="/nova-prijava" element={<Guard role="KANDIDAT"><NovaPrijava /></Guard>} />
            <Route path="/prijave/:id" element={<Guard role="KANDIDAT"><PrijavaDetalji /></Guard>} />

            
            <Route path="/admin/prijave" element={<Guard role="ADMIN"><AdminPrijave /></Guard>} />
            <Route path="/admin/prijave/:id" element={<Guard role="ADMIN"><AdminPrijavaDetalji /></Guard>} />
            <Route path="/admin/programi" element={<Guard role="ADMIN"><AdminProgrami /></Guard>} />
            <Route path="/admin/spisak" element={<Guard role="ADMIN"><AdminSpisak /></Guard>} />
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

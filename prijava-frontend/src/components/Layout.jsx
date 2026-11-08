import { NavLink, Outlet, Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function Layout() {
  const { user, isAdmin, logout } = useAuth()

  return (
    <>
      <header className="topbar">
        <Link to="/" className="brand">FON · Prijava na konkurs</Link>
        <nav>
          {isAdmin ? (
            <>
              <NavLink to="/admin/prijave">Prijave</NavLink>
              <NavLink to="/admin/programi">Studijski programi</NavLink>
              <NavLink to="/admin/spisak">Spisak kandidata</NavLink>
            </>
          ) : (
            <>
              <NavLink to="/moje-prijave">Moje prijave</NavLink>
              <NavLink to="/nova-prijava">Nova prijava</NavLink>
            </>
          )}
        </nav>
        <div className="user">
          <span>{user.ime} {user.prezime} · {isAdmin ? 'Administrator' : 'Kandidat'}</span>
          <button className="btn-text" onClick={logout}>Odjavi se</button>
        </div>
      </header>
      <main className="page">
        <Outlet />
      </main>
    </>
  )
}

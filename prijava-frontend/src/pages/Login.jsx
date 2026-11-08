import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import Field from '../components/Field'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [lozinka, setLozinka] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      const u = await login(email, lozinka)
      navigate(u.uloga === 'ADMIN' ? '/admin/prijave' : '/moje-prijave', { replace: true })
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-wrap">
      <form className="auth-box" onSubmit={submit} noValidate>
        <h1>Prijava na sistem</h1>
        <p className="sub">Konkurs za upis na osnovne akademske studije</p>

        {error && <div className="alert">{error}</div>}

        <Field label="Email">
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} autoFocus required />
        </Field>
        <Field label="Lozinka">
          <input type="password" value={lozinka} onChange={(e) => setLozinka(e.target.value)} required />
        </Field>

        <button type="submit" disabled={busy || !email || !lozinka}>
          {busy ? 'Prijavljivanje…' : 'Prijavi se'}
        </button>

        <p className="auth-switch">
          Nemate nalog? <Link to="/register">Registrujte se</Link>
        </p>
      </form>
    </div>
  )
}

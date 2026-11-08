import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { skoleApi } from '../api/services'
import Field from '../components/Field'

const prazno = {
  ime: '', prezime: '', email: '', lozinka: '', lozinka2: '',
  jmbg: '', datumRodjenja: '', adresa: '', telefon: '', srednjaSkolaId: '',
  prosek1Razred: '', prosek2Razred: '', prosek3Razred: '', prosek4Razred: '',
}

const PROSECI = [
  ['prosek1Razred', 'Prosek I razred (1.00–5.00)'],
  ['prosek2Razred', 'Prosek II razred (1.00–5.00)'],
  ['prosek3Razred', 'Prosek III razred (1.00–5.00)'],
  ['prosek4Razred', 'Prosek IV razred (1.00–5.00)'],
]

function validiraj(f) {
  const e = {}
  if (!f.ime.trim()) e.ime = 'Ime je obavezno'
  if (!f.prezime.trim()) e.prezime = 'Prezime je obavezno'
  if (!/^\S+@\S+\.\S+$/.test(f.email)) e.email = 'Email nije validan'
  if (f.lozinka.length < 6) e.lozinka = 'Lozinka mora imati bar 6 karaktera'
  if (f.lozinka !== f.lozinka2) e.lozinka2 = 'Lozinke se ne poklapaju'
  if (!/^\d{13}$/.test(f.jmbg)) e.jmbg = 'JMBG mora imati tačno 13 cifara'
  if (!f.datumRodjenja) e.datumRodjenja = 'Datum rođenja je obavezan'
  else if (new Date(f.datumRodjenja) >= new Date()) e.datumRodjenja = 'Datum rođenja mora biti u prošlosti'
  if (!f.srednjaSkolaId) e.srednjaSkolaId = 'Izaberite srednju školu'
  PROSECI.forEach(([k]) => {
    const p = parseFloat(f[k])
    if (isNaN(p) || p < 1 || p > 5) e[k] = 'Prosek mora biti između 1.00 i 5.00'
  })
  return e
}

export default function Register() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [f, setF] = useState(prazno)
  const [errors, setErrors] = useState({})
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const [skole, setSkole] = useState([])

  useEffect(() => {
    skoleApi.sve().then(setSkole).catch(() => {})
  }, [])

  const set = (k) => (e) => setF({ ...f, [k]: e.target.value })

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    const v = validiraj(f)
    setErrors(v)
    if (Object.keys(v).length) return

    setBusy(true)
    try {
      const { lozinka2, ...data } = f
      data.srednjaSkolaId = Number(f.srednjaSkolaId)
      PROSECI.forEach(([k]) => { data[k] = parseFloat(f[k]) })
      await register(data)
      navigate('/moje-prijave', { replace: true })
    } catch (err) {
      setError(err.message)
      if (err.fields) setErrors(err.fields)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-wrap">
      <form className="auth-box" style={{ maxWidth: 560 }} onSubmit={submit} noValidate>
        <h1>Registracija kandidata</h1>
        <p className="sub">Nalog je potreban za popunjavanje elektronske prijave.</p>

        {error && <div className="alert">{error}</div>}

        <h3>Nalog</h3>
        <div className="row">
          <Field label="Ime" error={errors.ime}><input value={f.ime} onChange={set('ime')} /></Field>
          <Field label="Prezime" error={errors.prezime}><input value={f.prezime} onChange={set('prezime')} /></Field>
        </div>
        <Field label="Email" error={errors.email}><input type="email" value={f.email} onChange={set('email')} /></Field>
        <div className="row">
          <Field label="Lozinka (min. 6 karaktera)" error={errors.lozinka}>
            <input type="password" value={f.lozinka} onChange={set('lozinka')} />
          </Field>
          <Field label="Ponovi lozinku" error={errors.lozinka2}>
            <input type="password" value={f.lozinka2} onChange={set('lozinka2')} />
          </Field>
        </div>

        <h3 style={{ marginTop: 8 }}>Podaci o kandidatu</h3>
        <div className="row">
          <Field label="JMBG" error={errors.jmbg}>
            <input value={f.jmbg} onChange={set('jmbg')} maxLength={13} inputMode="numeric" />
          </Field>
          <Field label="Datum rođenja" error={errors.datumRodjenja}>
            <input type="date" value={f.datumRodjenja} onChange={set('datumRodjenja')} />
          </Field>
        </div>
        <Field label="Adresa" error={errors.adresa}><input value={f.adresa} onChange={set('adresa')} /></Field>
        <div className="row">
          <Field label="Telefon" error={errors.telefon}><input value={f.telefon} onChange={set('telefon')} /></Field>
          <Field label="Srednja škola" error={errors.srednjaSkolaId}>
            <select value={f.srednjaSkolaId} onChange={set('srednjaSkolaId')}>
              <option value="">— izaberite školu —</option>
              {skole.map((s) => <option key={s.id} value={s.id}>{s.naziv}</option>)}
            </select>
          </Field>
        </div>
        <div className="row">
          {PROSECI.slice(0, 2).map(([k, label]) => (
            <Field key={k} label={label} error={errors[k]}>
              <input type="number" step="0.01" min="1" max="5" value={f[k]} onChange={set(k)} required />
            </Field>
          ))}
        </div>
        <div className="row">
          {PROSECI.slice(2).map(([k, label]) => (
            <Field key={k} label={label} error={errors[k]}>
              <input type="number" step="0.01" min="1" max="5" value={f[k]} onChange={set(k)} required />
            </Field>
          ))}
        </div>

        <button type="submit" disabled={busy}>{busy ? 'Kreiranje naloga…' : 'Registruj se'}</button>

        <p className="auth-switch">
          Već imate nalog? <Link to="/login">Prijavite se</Link>
        </p>
      </form>
    </div>
  )
}

import { Fragment, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'
import {
  kandidatiApi, programiApi, prijaveApi, prijavaProgramApi, dokumentiApi, skoleApi, TIPOVI_DOKUMENATA,
} from '../../api/services'
import Field from '../../components/Field'

const PROSECI = [
  ['prosek1Razred', 'Prosek I razred'],
  ['prosek2Razred', 'Prosek II razred'],
  ['prosek3Razred', 'Prosek III razred'],
  ['prosek4Razred', 'Prosek IV razred'],
]

const MAX_PROGRAMA = 2


const KORACI = ['Lični podaci', 'Studijski program', 'Dokumenta', 'Pregled i podnošenje']

export default function NovaPrijava() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [korak, setKorak] = useState(0)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  
  const [kandidat, setKandidat] = useState(null)
  const [skole, setSkole] = useState([])
  const [errors, setErrors] = useState({})
  
  const [programi, setProgrami] = useState([])
  const [izabrani, setIzabrani] = useState([])
  
  const [fajlovi, setFajlovi] = useState({})
  
  const [saglasan, setSaglasan] = useState(false)

  useEffect(() => {
    Promise.all([kandidatiApi.jedan(user.kandidatId), programiApi.sve(), skoleApi.sve()])
      .then(([k, p, s]) => {
        setKandidat({ ...k, datumRodjenja: k.datumRodjenja || '', srednjaSkolaId: k.srednjaSkola?.id ? String(k.srednjaSkola.id) : '' })
        setProgrami(p)
        setSkole(s)
      })
      .catch((e) => setError(e.message))
  }, [user.kandidatId])

  const setK = (k) => (e) => setKandidat({ ...kandidat, [k]: e.target.value })

  
  const sacuvajPodatke = async () => {
    const e = {}
    if (!/^\d{13}$/.test(kandidat.jmbg)) e.jmbg = 'JMBG mora imati tačno 13 cifara'
    if (!kandidat.datumRodjenja) e.datumRodjenja = 'Datum rođenja je obavezan'
    if (!kandidat.adresa?.trim()) e.adresa = 'Adresa je obavezna za prijavu'
    if (!kandidat.telefon?.trim()) e.telefon = 'Telefon je obavezan za prijavu'
    if (!kandidat.srednjaSkolaId) e.srednjaSkolaId = 'Izaberite srednju školu'
    const proseci = {}
    PROSECI.forEach(([k]) => {
      const p = parseFloat(kandidat[k])
      if (isNaN(p) || p < 1 || p > 5) e[k] = 'Prosek mora biti između 1.00 i 5.00'
      proseci[k] = p
    })
    setErrors(e)
    if (Object.keys(e).length) return

    setBusy(true)
    setError('')
    try {
      const { srednjaSkolaId, ...ostalo } = kandidat
      await kandidatiApi.izmeni(kandidat.id, {
        ...ostalo,
        ...proseci,
        srednjaSkola: { id: Number(srednjaSkolaId) },
        korisnik: { id: kandidat.korisnik.id },
      })
      setKorak(1)
    } catch (err) {
      setError(err.message)
      if (err.fields) setErrors(err.fields)
    } finally {
      setBusy(false)
    }
  }

  const toggleProgram = (id) => {
    if (izabrani.includes(id)) return setIzabrani(izabrani.filter((x) => x !== id))
    if (izabrani.length >= MAX_PROGRAMA) return
    setIzabrani([...izabrani, id])
  }

  const setFajl = (tip) => (e) => setFajlovi({ ...fajlovi, [tip]: e.target.files[0] || undefined })

  const obavezni = ['SVEDOCANSTVO_1', 'SVEDOCANSTVO_2', 'SVEDOCANSTVO_3', 'SVEDOCANSTVO_4', 'DIPLOMA']
  const nedostaju = obavezni.filter((t) => !fajlovi[t])

  
  const podnesi = async () => {
    setBusy(true)
    setError('')
    try {
      const prijava = await prijaveApi.kreiraj(user.kandidatId)
      for (const programId of izabrani) {
        await prijavaProgramApi.dodaj(prijava.id, programId)
      }
      for (const [tip, file] of Object.entries(fajlovi)) {
        if (file) await dokumentiApi.upload(prijava.id, tip, file)
      }
      navigate(`/prijave/${prijava.id}`, { replace: true, state: { podneta: true } })
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  if (!kandidat) return <p className="muted">{error || 'Učitavanje…'}</p>

  return (
    <>
      <h1>Elektronska prijava</h1>
      <p className="muted">Popunite prijavu korak po korak. Podaci se čuvaju tek na kraju.</p>

      <div className="steps">
        {KORACI.map((n, i) => (
          <div key={n} className={i === korak ? 'current' : i < korak ? 'done' : ''}>
            {i + 1}. {n}
          </div>
        ))}
      </div>

      {error && <div className="alert">{error}</div>}

      
      {korak === 0 && (
        <div>
          <h2>Proverite lične podatke</h2>
          <div className="row">
            <Field label="Ime"><input value={kandidat.korisnik.ime} disabled /></Field>
            <Field label="Prezime"><input value={kandidat.korisnik.prezime} disabled /></Field>
          </div>
          <Field label="Email"><input value={kandidat.korisnik.email} disabled /></Field>
          <div className="row">
            <Field label="JMBG" error={errors.jmbg}>
              <input value={kandidat.jmbg} onChange={setK('jmbg')} maxLength={13} />
            </Field>
            <Field label="Datum rođenja" error={errors.datumRodjenja}>
              <input type="date" value={kandidat.datumRodjenja} onChange={setK('datumRodjenja')} />
            </Field>
          </div>
          <Field label="Adresa stanovanja" error={errors.adresa}>
            <input value={kandidat.adresa || ''} onChange={setK('adresa')} />
          </Field>
          <Field label="Telefon" error={errors.telefon}>
            <input value={kandidat.telefon || ''} onChange={setK('telefon')} />
          </Field>
          <Field label="Završena srednja škola" error={errors.srednjaSkolaId}>
            <select value={kandidat.srednjaSkolaId} onChange={setK('srednjaSkolaId')}>
              <option value="">— izaberite školu —</option>
              {skole.map((s) => <option key={s.id} value={s.id}>{s.naziv}</option>)}
            </select>
          </Field>
          <div className="row">
            {PROSECI.slice(0, 2).map(([k, label]) => (
              <Field key={k} label={label} error={errors[k]}>
                <input type="number" step="0.01" min="1" max="5" value={kandidat[k] ?? ''} onChange={setK(k)} required />
              </Field>
            ))}
          </div>
          <div className="row">
            {PROSECI.slice(2).map(([k, label]) => (
              <Field key={k} label={label} error={errors[k]}>
                <input type="number" step="0.01" min="1" max="5" value={kandidat[k] ?? ''} onChange={setK(k)} required />
              </Field>
            ))}
          </div>
          <div className="actions">
            <button onClick={sacuvajPodatke} disabled={busy}>Sačuvaj i nastavi</button>
          </div>
        </div>
      )}

      
      {korak === 1 && (
        <div>
          <h2>Na koji studijski program konkurišete?</h2>
          <p className="muted">Možete izabrati jedan ili oba programa (najviše {MAX_PROGRAMA}).</p>
          {programi.length === 0 && <div className="empty">Trenutno nema otvorenih studijskih programa.</div>}
          {programi.map((p) => (
            <label key={p.id} className={'choice' + (izabrani.includes(p.id) ? ' selected' : '')}>
              <input
                type="checkbox"
                checked={izabrani.includes(p.id)}
                disabled={!izabrani.includes(p.id) && izabrani.length >= MAX_PROGRAMA}
                onChange={() => toggleProgram(p.id)}
              />
              <div>
                <strong>{p.naziv}</strong>
                <span>{p.opis}{p.opis ? ' · ' : ''}Broj mesta: {p.brojMesta}</span>
              </div>
            </label>
          ))}
          <div className="actions between">
            <button className="btn-outline" onClick={() => setKorak(0)}>Nazad</button>
            <button onClick={() => setKorak(2)} disabled={izabrani.length === 0 || izabrani.length > MAX_PROGRAMA}>Nastavi</button>
          </div>
        </div>
      )}

      
      {korak === 2 && (
        <div>
          <h2>Priložite dokumentaciju</h2>
          <p className="muted">Svedočanstva sva četiri razreda i diploma su obavezni. Dozvoljeni formati: PDF ili slika, do 10 MB.</p>
          {TIPOVI_DOKUMENATA.map((t) => (
            <Field key={t.key} label={t.label + (obavezni.includes(t.key) ? ' *' : '')}>
              <input type="file" accept=".pdf,image/*" onChange={setFajl(t.key)} />
              {fajlovi[t.key] && <span className="small muted">{fajlovi[t.key].name}</span>}
            </Field>
          ))}
          <div className="actions between">
            <button className="btn-outline" onClick={() => setKorak(1)}>Nazad</button>
            <button onClick={() => setKorak(3)} disabled={nedostaju.length > 0}>
              {nedostaju.length > 0 ? `Nedostaje još ${nedostaju.length}` : 'Nastavi'}
            </button>
          </div>
        </div>
      )}

      
      {korak === 3 && (
        <div>
          <h2>Pregledajte prijavu pre podnošenja</h2>

          <div className="card">
            <h3>Kandidat</h3>
            <dl className="dl">
              <dt>Ime i prezime</dt><dd>{kandidat.korisnik.ime} {kandidat.korisnik.prezime}</dd>
              <dt>JMBG</dt><dd>{kandidat.jmbg}</dd>
              <dt>Datum rođenja</dt><dd>{kandidat.datumRodjenja}</dd>
              <dt>Adresa</dt><dd>{kandidat.adresa}</dd>
              <dt>Telefon</dt><dd>{kandidat.telefon}</dd>
              <dt>Srednja škola</dt><dd>{skole.find((s) => String(s.id) === String(kandidat.srednjaSkolaId))?.naziv || '—'}</dd>
              {PROSECI.map(([k, label]) => (
                <Fragment key={k}><dt>{label}</dt><dd>{Number(kandidat[k]).toFixed(2)}</dd></Fragment>
              ))}
            </dl>
          </div>

          <div className="card">
            <h3>Studijski programi</h3>
            <ul>
              {programi.filter((p) => izabrani.includes(p.id)).map((p) => <li key={p.id}>{p.naziv}</li>)}
            </ul>
          </div>

          <div className="card">
            <h3>Dokumenta</h3>
            <ul>
              {TIPOVI_DOKUMENATA.filter((t) => fajlovi[t.key]).map((t) => (
                <li key={t.key}>{t.label} — <span className="muted">{fajlovi[t.key].name}</span></li>
              ))}
            </ul>
          </div>

          <label className="choice">
            <input type="checkbox" checked={saglasan} onChange={(e) => setSaglasan(e.target.checked)} />
            <div>
              <strong>Izjavljujem da su uneti podaci tačni</strong>
              <span>Nakon podnošenja prijava dobija status „Podneta“ i administrator pregleda dokumentaciju.</span>
            </div>
          </label>

          <div className="actions between">
            <button className="btn-outline" onClick={() => setKorak(2)} disabled={busy}>Nazad</button>
            <button onClick={podnesi} disabled={!saglasan || busy}>
              {busy ? 'Podnošenje…' : 'Podnesi prijavu'}
            </button>
          </div>
        </div>
      )}
    </>
  )
}

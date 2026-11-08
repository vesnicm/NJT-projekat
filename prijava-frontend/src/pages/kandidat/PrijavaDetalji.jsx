import { useCallback, useEffect, useState } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'
import {
  prijaveApi, prijavaProgramApi, dokumentiApi, formatDatum, TIPOVI_DOKUMENATA,
} from '../../api/services'
import StatusBadge from '../../components/StatusBadge'
import DokumentiLista from '../../components/DokumentiLista'
import Field from '../../components/Field'

export default function PrijavaDetalji() {
  const { id } = useParams()
  const { user } = useAuth()
  const location = useLocation()
  const [prijava, setPrijava] = useState(null)
  const [programi, setProgrami] = useState([])
  const [dokumenti, setDokumenti] = useState([])
  const [error, setError] = useState('')
  const [tip, setTip] = useState(TIPOVI_DOKUMENATA[0].key)
  const [file, setFile] = useState(null)
  const [busy, setBusy] = useState(false)

  const ucitaj = useCallback(async () => {
    try {
      const p = await prijaveApi.jedna(id)
      if (p.kandidat?.id !== user.kandidatId) {
        setError('Nemate pristup ovoj prijavi.')
        return
      }
      setPrijava(p)
      setProgrami(await prijavaProgramApi.zaPrijavu(id))
      setDokumenti(await dokumentiApi.zaPrijavu(id))
    } catch (e) {
      setError(e.message)
    }
  }, [id, user.kandidatId])

  useEffect(() => { ucitaj() }, [ucitaj])

  const dopuni = async (e) => {
    e.preventDefault()
    if (!file) return
    setBusy(true)
    setError('')
    try {
      await dokumentiApi.upload(id, tip, file)
      setFile(null)
      e.target.reset()
      await ucitaj()
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  if (error && !prijava) return <div className="alert">{error}</div>
  if (!prijava) return <p className="muted">Učitavanje…</p>

  const otvorena = prijava.status === 'PODNETA' || prijava.status === 'POTREBNA_DOPUNA'

  return (
    <>
      <p className="small"><Link to="/moje-prijave">← Moje prijave</Link></p>
      <div className="header-line">
        <h1>Prijava #{prijava.id}</h1>
        <StatusBadge status={prijava.status} />
      </div>

      {location.state?.podneta && (
        <div className="alert soft">Prijava je uspešno podneta. Status možete pratiti na ovoj stranici.</div>
      )}
      {error && <div className="alert">{error}</div>}

      {prijava.status === 'POTREBNA_DOPUNA' && (
        <div className="alert">
          <strong>Administrator traži dopunu dokumentacije.</strong>
          {prijava.napomena && <div style={{ marginTop: 6 }}>{prijava.napomena}</div>}
        </div>
      )}
      {prijava.status === 'ODBIJENA' && prijava.napomena && (
        <div className="alert soft"><strong>Razlog:</strong> {prijava.napomena}</div>
      )}
      {prijava.status === 'PRIHVACENA' && (
        <div className="alert soft">Prijava je kompletna. Nalazite se na spisku prijavljenih kandidata za izabrane programe.</div>
      )}

      <div className="card">
        <h2>Podaci o prijavi</h2>
        <dl className="dl">
          <dt>Podneta</dt><dd>{formatDatum(prijava.datumPodnosenja)}</dd>
          <dt>Studijski programi</dt>
          <dd>{programi.map((pp) => pp.studijskiProgram.naziv).join(', ') || '—'}</dd>
          <dt>Kandidat</dt>
          <dd>{prijava.kandidat.korisnik.ime} {prijava.kandidat.korisnik.prezime}, JMBG {prijava.kandidat.jmbg}</dd>
        </dl>
      </div>

      <div className="card">
        <h2>Dokumenta</h2>
        <DokumentiLista dokumenti={dokumenti} canDelete={otvorena} onChange={ucitaj} />
      </div>

      {otvorena && (
        <form className="card" onSubmit={dopuni}>
          <h2>Dopuni dokumentaciju</h2>
          <div className="row">
            <Field label="Vrsta dokumenta">
              <select value={tip} onChange={(e) => setTip(e.target.value)}>
                {TIPOVI_DOKUMENATA.map((t) => <option key={t.key} value={t.key}>{t.label}</option>)}
              </select>
            </Field>
            <Field label="Fajl (PDF ili slika, do 10 MB)">
              <input type="file" accept=".pdf,image/*" onChange={(e) => setFile(e.target.files[0])} />
            </Field>
          </div>
          <button type="submit" disabled={!file || busy}>{busy ? 'Slanje…' : 'Priloži dokument'}</button>
        </form>
      )}
    </>
  )
}

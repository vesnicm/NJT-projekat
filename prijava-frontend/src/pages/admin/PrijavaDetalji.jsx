import { useCallback, useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { prijaveApi, prijavaProgramApi, dokumentiApi, STATUSI, formatDatum, prosek4 } from '../../api/services'
import StatusBadge from '../../components/StatusBadge'
import DokumentiLista from '../../components/DokumentiLista'
import Field from '../../components/Field'

export default function PrijavaDetalji() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [prijava, setPrijava] = useState(null)
  const [programi, setProgrami] = useState([])
  const [dokumenti, setDokumenti] = useState([])
  const [error, setError] = useState('')
  const [noviStatus, setNoviStatus] = useState('')
  const [napomena, setNapomena] = useState('')
  const [busy, setBusy] = useState(false)

  const ucitaj = useCallback(async () => {
    try {
      const p = await prijaveApi.jedna(id)
      setPrijava(p)
      setNoviStatus(p.status)
      setNapomena(p.napomena || '')
      setProgrami(await prijavaProgramApi.zaPrijavu(id))
      setDokumenti(await dokumentiApi.zaPrijavu(id))
    } catch (e) {
      setError(e.message)
    }
  }, [id])

  useEffect(() => { ucitaj() }, [ucitaj])

  const sacuvajStatus = async (e) => {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      await prijaveApi.promeniStatus(id, noviStatus, napomena)
      await ucitaj()
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  const obrisi = async () => {
    if (!window.confirm('Obrisati ovu prijavu zajedno sa dokumentima?')) return
    try {
      await prijaveApi.obrisi(id)
      navigate('/admin/prijave')
    } catch (err) {
      setError(err.message)
    }
  }

  if (error && !prijava) return <div className="alert">{error}</div>
  if (!prijava) return <p className="muted">Učitavanje…</p>

  const k = prijava.kandidat
  const sviVerifikovani = dokumenti.length > 0 && dokumenti.every((d) => d.verifikovan)

  return (
    <>
      <p className="small"><Link to="/admin/prijave">← Sve prijave</Link></p>
      <div className="header-line">
        <h1>Prijava #{prijava.id}</h1>
        <StatusBadge status={prijava.status} />
      </div>

      {error && <div className="alert">{error}</div>}

      <div className="card">
        <h2>Kandidat</h2>
        <dl className="dl">
          <dt>Ime i prezime</dt><dd>{k.korisnik.ime} {k.korisnik.prezime}</dd>
          <dt>Email</dt><dd>{k.korisnik.email}</dd>
          <dt>JMBG</dt><dd>{k.jmbg}</dd>
          <dt>Datum rođenja</dt><dd>{k.datumRodjenja}</dd>
          <dt>Adresa</dt><dd>{k.adresa || '—'}</dd>
          <dt>Telefon</dt><dd>{k.telefon || '—'}</dd>
          <dt>Srednja škola</dt><dd>{k.srednjaSkola?.naziv || '—'}</dd>
          <dt>Prosek I razred</dt><dd>{Number(k.prosek1Razred).toFixed(2)}</dd>
          <dt>Prosek II razred</dt><dd>{Number(k.prosek2Razred).toFixed(2)}</dd>
          <dt>Prosek III razred</dt><dd>{Number(k.prosek3Razred).toFixed(2)}</dd>
          <dt>Prosek IV razred</dt><dd>{Number(k.prosek4Razred).toFixed(2)}</dd>
          <dt>Prosek (I–IV)</dt><dd>{prosek4(k).toFixed(2)}</dd>
          <dt>Studijski programi</dt><dd>{programi.map((pp) => pp.studijskiProgram.naziv).join(', ') || '—'}</dd>
        </dl>
      </div>

      <div className="card">
        <h2>Podaci o prijavi</h2>
        <dl className="dl">
          <dt>Broj prijave</dt><dd>{prijava.brojPrijave || '—'}</dd>
          <dt>Cena</dt><dd>{Number(prijava.cena).toFixed(2)} RSD</dd>
          <dt>Podneta</dt><dd>{formatDatum(prijava.datumPodnosenja)}</dd>
          <dt>Pregledao</dt><dd>{prijava.pregledao ? `${prijava.pregledao.ime} ${prijava.pregledao.prezime}` : '—'}</dd>
        </dl>
      </div>

      <div className="card">
        <h2>Dokumentacija</h2>
        <p className="small muted">
          {dokumenti.filter((d) => d.verifikovan).length} od {dokumenti.length} verifikovano
          {sviVerifikovani ? ' — dokumentacija kompletna' : ''}
        </p>
        <DokumentiLista dokumenti={dokumenti} adminMode onChange={ucitaj} />
      </div>

      <form className="card" onSubmit={sacuvajStatus}>
        <h2>Status prijave</h2>
        <div className="row">
          <Field label="Novi status">
            <select value={noviStatus} onChange={(e) => setNoviStatus(e.target.value)}>
              {Object.entries(STATUSI).map(([key, v]) => <option key={key} value={key}>{v}</option>)}
            </select>
          </Field>
        </div>
        <Field label={noviStatus === 'POTREBNA_DOPUNA' ? 'Šta kandidat treba da dopuni ili izmeni' : 'Napomena (vidljiva kandidatu)'}>
          <textarea rows={3} value={napomena} onChange={(e) => setNapomena(e.target.value)} />
        </Field>
        <div className="actions between">
          <button type="submit" disabled={busy || (noviStatus === prijava.status && napomena === (prijava.napomena || ''))}>
            Sačuvaj status
          </button>
          <button type="button" className="btn-text" onClick={obrisi}>Obriši prijavu</button>
        </div>
      </form>
    </>
  )
}

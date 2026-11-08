import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { prijaveApi, programiApi, STATUSI, formatDatum, prosek4 } from '../../api/services'
import StatusBadge from '../../components/StatusBadge'
import Field from '../../components/Field'

const praznoFilter = { status: '', jmbg: '', ime: '', prezime: '', programId: '' }

export default function Prijave() {
  const navigate = useNavigate()
  const [filter, setFilter] = useState(praznoFilter)
  const [programi, setProgrami] = useState([])
  const [prijave, setPrijave] = useState(null)
  const [error, setError] = useState('')
  const [sort, setSort] = useState({ key: 'datumPodnosenja', dir: -1 })

  const set = (k) => (e) => setFilter({ ...filter, [k]: e.target.value })

  const pretrazi = async (f = filter) => {
    setError('')
    try {
      setPrijave(await prijaveApi.pretraga(f))
    } catch (e) {
      setError(e.message)
    }
  }

  useEffect(() => {
    programiApi.sve().then(setProgrami).catch(() => {})
    pretrazi(praznoFilter)
  }, [])

  const submit = (e) => { e.preventDefault(); pretrazi() }
  const reset = () => { setFilter(praznoFilter); pretrazi(praznoFilter) }

  const sortiraj = (key) => setSort(sort.key === key ? { key, dir: -sort.dir } : { key, dir: 1 })

  const vrednost = (p, key) => {
    switch (key) {
      case 'id': return p.id
      case 'kandidat': return `${p.kandidat?.korisnik?.prezime} ${p.kandidat?.korisnik?.ime}`.toLowerCase()
      case 'prosek': return prosek4(p.kandidat)
      case 'status': return p.status
      default: return p.datumPodnosenja || ''
    }
  }
  const lista = prijave
    ? [...prijave].sort((a, b) => (vrednost(a, sort.key) > vrednost(b, sort.key) ? 1 : -1) * sort.dir)
    : null

  const th = (key, label) => (
    <th style={{ cursor: 'pointer' }} onClick={() => sortiraj(key)}>
      {label}{sort.key === key ? (sort.dir > 0 ? ' ↑' : ' ↓') : ''}
    </th>
  )

  const brojevi = prijave ? prijave.reduce((m, p) => ({ ...m, [p.status]: (m[p.status] || 0) + 1 }), {}) : {}

  return (
    <>
      <div className="header-line">
        <h1>Prijave kandidata</h1>
        {prijave && (
          <span className="small muted">
            Ukupno {prijave.length} ·{' '}
            {Object.entries(STATUSI).map(([k, v]) => `${v}: ${brojevi[k] || 0}`).join(' · ')}
          </span>
        )}
      </div>

      <form className="toolbar" onSubmit={submit}>
        <Field label="Status">
          <select value={filter.status} onChange={set('status')}>
            <option value="">Svi</option>
            {Object.entries(STATUSI).map(([k, v]) => <option key={k} value={k}>{v}</option>)}
          </select>
        </Field>
        <Field label="Studijski program">
          <select value={filter.programId} onChange={set('programId')}>
            <option value="">Svi</option>
            {programi.map((p) => <option key={p.id} value={p.id}>{p.naziv}</option>)}
          </select>
        </Field>
        <Field label="Ime"><input value={filter.ime} onChange={set('ime')} /></Field>
        <Field label="Prezime"><input value={filter.prezime} onChange={set('prezime')} /></Field>
        <Field label="JMBG"><input value={filter.jmbg} onChange={set('jmbg')} maxLength={13} /></Field>
        <button type="submit">Pretraži</button>
        <button type="button" className="btn-outline" onClick={reset}>Poništi</button>
      </form>

      {error && <div className="alert">{error}</div>}

      {lista === null ? (
        <p className="muted">Učitavanje…</p>
      ) : lista.length === 0 ? (
        <div className="empty">Nema prijava koje odgovaraju kriterijumima.</div>
      ) : (
        <table>
          <thead>
            <tr>
              {th('id', 'Broj')}
              {th('kandidat', 'Kandidat')}
              <th>JMBG</th>
              {th('prosek', 'Prosek')}
              {th('datumPodnosenja', 'Podneta')}
              {th('status', 'Status')}
            </tr>
          </thead>
          <tbody>
            {lista.map((p) => (
              <tr key={p.id} className="clickable" onClick={() => navigate(`/admin/prijave/${p.id}`)}>
                <td>#{p.id}</td>
                <td>{p.kandidat?.korisnik?.prezime} {p.kandidat?.korisnik?.ime}</td>
                <td>{p.kandidat?.jmbg}</td>
                <td>{prosek4(p.kandidat).toFixed(2)}</td>
                <td>{formatDatum(p.datumPodnosenja)}</td>
                <td><StatusBadge status={p.status} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  )
}

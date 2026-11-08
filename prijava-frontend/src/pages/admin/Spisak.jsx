import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { programiApi, prijavaProgramApi, STATUSI, prosek4 } from '../../api/services'
import StatusBadge from '../../components/StatusBadge'
import Field from '../../components/Field'


export default function Spisak() {
  const [programi, setProgrami] = useState([])
  const [programId, setProgramId] = useState('')
  const [samoPrihvacene, setSamoPrihvacene] = useState(true)
  const [stavke, setStavke] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    programiApi.sve()
      .then((p) => { setProgrami(p); if (p.length) setProgramId(String(p[0].id)) })
      .catch((e) => setError(e.message))
  }, [])

  useEffect(() => {
    if (!programId) return
    setStavke(null)
    prijavaProgramApi.zaProgram(programId).then(setStavke).catch((e) => setError(e.message))
  }, [programId])

  const program = programi.find((p) => String(p.id) === programId)
  const lista = (stavke || [])
    .filter((s) => !samoPrihvacene || s.prijava.status === 'PRIHVACENA')
    .sort((a, b) => (prosek4(b.prijava.kandidat) - prosek4(a.prijava.kandidat)))

  const exportCsv = () => {
    const redovi = [['R.br.', 'Prezime', 'Ime', 'JMBG', 'Srednja škola', 'Prosek', 'Status']]
    lista.forEach((s, i) => {
      const k = s.prijava.kandidat
      redovi.push([i + 1, k.korisnik.prezime, k.korisnik.ime, k.jmbg, k.srednjaSkola?.naziv || '', prosek4(k).toFixed(2), STATUSI[s.prijava.status]])
    })
    const csv = redovi.map((r) => r.map((c) => `"${String(c).replace(/"/g, '""')}"`).join(';')).join('\n')
    const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8' })
    const a = document.createElement('a')
    a.href = URL.createObjectURL(blob)
    a.download = `spisak-${(program?.naziv || 'program').replace(/\s+/g, '-')}.csv`
    a.click()
  }

  return (
    <>
      <h1>Spisak prijavljenih kandidata</h1>

      {error && <div className="alert">{error}</div>}

      <div className="toolbar">
        <Field label="Studijski program">
          <select value={programId} onChange={(e) => setProgramId(e.target.value)}>
            {programi.map((p) => <option key={p.id} value={p.id}>{p.naziv}</option>)}
          </select>
        </Field>
        <Field label="Prikaz">
          <select value={samoPrihvacene ? '1' : '0'} onChange={(e) => setSamoPrihvacene(e.target.value === '1')}>
            <option value="1">Samo prihvaćene prijave</option>
            <option value="0">Sve prijave</option>
          </select>
        </Field>
        <button className="btn-outline" onClick={exportCsv} disabled={!lista.length}>Izvezi CSV</button>
      </div>

      {program && (
        <p className="small muted">
          {program.naziv} · broj mesta: {program.brojMesta} · na spisku: {lista.length}
        </p>
      )}

      {stavke === null ? (
        <p className="muted">Učitavanje…</p>
      ) : lista.length === 0 ? (
        <div className="empty">Nema kandidata za izabrani program.</div>
      ) : (
        <table>
          <thead>
            <tr><th>R.br.</th><th>Kandidat</th><th>JMBG</th><th>Srednja škola</th><th>Prosek</th><th>Status</th></tr>
          </thead>
          <tbody>
            {lista.map((s, i) => {
              const k = s.prijava.kandidat
              return (
                <tr key={s.id}>
                  <td>{i + 1}.</td>
                  <td><Link to={`/admin/prijave/${s.prijava.id}`}>{k.korisnik.prezime} {k.korisnik.ime}</Link></td>
                  <td>{k.jmbg}</td>
                  <td>{k.srednjaSkola?.naziv || '—'}</td>
                  <td>{prosek4(k).toFixed(2)}</td>
                  <td><StatusBadge status={s.prijava.status} /></td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}
    </>
  )
}

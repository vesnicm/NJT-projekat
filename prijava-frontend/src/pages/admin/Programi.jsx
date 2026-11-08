import { useEffect, useState } from 'react'
import { programiApi } from '../../api/services'
import Field from '../../components/Field'

const prazno = { naziv: '', opis: '', brojMesta: '' }

export default function Programi() {
  const [programi, setProgrami] = useState(null)
  const [forma, setForma] = useState(prazno)
  const [editId, setEditId] = useState(null)
  const [errors, setErrors] = useState({})
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  const ucitaj = () => programiApi.sve().then(setProgrami).catch((e) => setError(e.message))
  useEffect(() => { ucitaj() }, [])

  const set = (k) => (e) => setForma({ ...forma, [k]: e.target.value })

  const izmeni = (p) => {
    setEditId(p.id)
    setForma({ naziv: p.naziv, opis: p.opis || '', brojMesta: p.brojMesta })
    setErrors({})
    window.scrollTo(0, 0)
  }
  const odustani = () => { setEditId(null); setForma(prazno); setErrors({}) }

  const submit = async (e) => {
    e.preventDefault()
    const v = {}
    if (!forma.naziv.trim()) v.naziv = 'Naziv je obavezan'
    const bm = parseInt(forma.brojMesta, 10)
    if (isNaN(bm) || bm < 0) v.brojMesta = 'Broj mesta ne može biti negativan'
    setErrors(v)
    if (Object.keys(v).length) return

    setBusy(true)
    setError('')
    try {
      const data = { naziv: forma.naziv.trim(), opis: forma.opis.trim(), brojMesta: bm }
      if (editId) await programiApi.izmeni(editId, data)
      else await programiApi.kreiraj(data)
      odustani()
      await ucitaj()
    } catch (err) {
      setError(err.message)
      if (err.fields) setErrors(err.fields)
    } finally {
      setBusy(false)
    }
  }

  const obrisi = async (p) => {
    if (!window.confirm(`Obrisati program "${p.naziv}"?`)) return
    setError('')
    try {
      await programiApi.obrisi(p.id)
      await ucitaj()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <>
      <h1>Studijski programi</h1>

      {error && <div className="alert">{error}</div>}

      <form className="card" onSubmit={submit}>
        <h2>{editId ? 'Izmena programa' : 'Novi program'}</h2>
        <div className="row">
          <Field label="Naziv" error={errors.naziv}><input value={forma.naziv} onChange={set('naziv')} /></Field>
          <Field label="Broj mesta" error={errors.brojMesta}>
            <input type="number" min="0" value={forma.brojMesta} onChange={set('brojMesta')} />
          </Field>
        </div>
        <Field label="Opis" error={errors.opis}><textarea rows={2} value={forma.opis} onChange={set('opis')} /></Field>
        <div className="actions">
          <button type="submit" disabled={busy}>{editId ? 'Sačuvaj izmene' : 'Dodaj program'}</button>
          {editId && <button type="button" className="btn-outline" onClick={odustani}>Odustani</button>}
        </div>
      </form>

      {programi === null ? (
        <p className="muted">Učitavanje…</p>
      ) : programi.length === 0 ? (
        <div className="empty">Nema unetih studijskih programa.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Naziv</th><th>Opis</th><th>Broj mesta</th><th></th></tr>
          </thead>
          <tbody>
            {programi.map((p) => (
              <tr key={p.id}>
                <td>{p.naziv}</td>
                <td className="muted">{p.opis || '—'}</td>
                <td>{p.brojMesta}</td>
                <td style={{ textAlign: 'right', whiteSpace: 'nowrap' }}>
                  <button className="btn-outline btn-sm" onClick={() => izmeni(p)}>Izmeni</button>{' '}
                  <button className="btn-text btn-sm" onClick={() => obrisi(p)}>Obriši</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  )
}

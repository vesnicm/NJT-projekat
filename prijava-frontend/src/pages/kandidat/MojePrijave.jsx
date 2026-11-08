import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'
import { prijaveApi, prijavaProgramApi, formatDatum } from '../../api/services'
import StatusBadge from '../../components/StatusBadge'

export default function MojePrijave() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [prijave, setPrijave] = useState(null)
  const [programi, setProgrami] = useState({})
  const [error, setError] = useState('')

  useEffect(() => {
    if (!user.kandidatId) { setPrijave([]); return }
    prijaveApi.zaKandidata(user.kandidatId)
      .then(async (lista) => {
        setPrijave(lista)
        const m = {}
        await Promise.all(lista.map(async (p) => {
          m[p.id] = await prijavaProgramApi.zaPrijavu(p.id)
        }))
        setProgrami(m)
      })
      .catch((e) => setError(e.message))
  }, [user.kandidatId])

  return (
    <>
      <div className="header-line">
        <h1>Moje prijave</h1>
        <Link className="btn" to="/nova-prijava">Nova prijava</Link>
      </div>

      {error && <div className="alert">{error}</div>}

      {prijave === null ? (
        <p className="muted">Učitavanje…</p>
      ) : prijave.length === 0 ? (
        <div className="empty">
          Još nemate nijednu prijavu. <Link to="/nova-prijava">Popunite elektronsku prijavu</Link>.
        </div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Broj</th>
              <th>Studijski programi</th>
              <th>Podneta</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {prijave.map((p) => (
              <tr key={p.id} className="clickable" onClick={() => navigate(`/prijave/${p.id}`)}>
                <td>#{p.id}</td>
                <td>{(programi[p.id] || []).map((pp) => pp.studijskiProgram.naziv).join(', ') || '—'}</td>
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

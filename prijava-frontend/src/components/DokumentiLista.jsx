import { dokumentiApi, labelTipa, formatDatum } from '../api/services'

export default function DokumentiLista({ dokumenti, adminMode, canDelete, onChange }) {
  const preuzmi = async (d) => {
    const blob = await dokumentiApi.preuzmi(d.id)
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = d.nazivFajla
    a.click()
    URL.revokeObjectURL(url)
  }

  const verifikuj = async (d, v) => {
    await dokumentiApi.verifikuj(d.id, v)
    onChange && onChange()
  }

  const obrisi = async (d) => {
    if (!window.confirm(`Obrisati "${d.nazivFajla}"?`)) return
    await dokumentiApi.obrisi(d.id)
    onChange && onChange()
  }

  if (!dokumenti.length) return <div className="empty">Nema priloženih dokumenata.</div>

  return (
    <ul className="doc-list">
      {dokumenti.map((d) => (
        <li key={d.id}>
          <div>
            <div>{labelTipa(d.tip)}</div>
            <div className="meta">
              {d.nazivFajla} · {formatDatum(d.datumUploada)} ·{' '}
              {d.verifikovan ? 'verifikovan' : 'nije verifikovan'}
            </div>
          </div>
          <div className="right">
            <button className="btn-outline btn-sm" onClick={() => preuzmi(d)}>Preuzmi</button>
            {adminMode && (
              d.verifikovan
                ? <button className="btn-outline btn-sm" onClick={() => verifikuj(d, false)}>Poništi</button>
                : <button className="btn-sm" onClick={() => verifikuj(d, true)}>Verifikuj</button>
            )}
            {canDelete && (
              <button className="btn-text btn-sm" onClick={() => obrisi(d)}>Obriši</button>
            )}
          </div>
        </li>
      ))}
    </ul>
  )
}

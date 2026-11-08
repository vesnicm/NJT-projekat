import { STATUSI } from '../api/services'

export default function StatusBadge({ status }) {
  if (!status) return null
  return <span className={'status ' + status}>{STATUSI[status] || status}</span>
}

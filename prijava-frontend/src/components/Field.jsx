export default function Field({ label, error, children }) {
  return (
    <div className={'field' + (error ? ' has-error' : '')}>
      {label && <label>{label}</label>}
      {children}
      {error && <div className="error">{error}</div>}
    </div>
  )
}

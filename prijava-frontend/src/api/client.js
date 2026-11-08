const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export function getToken() {
  return localStorage.getItem('token')
}

export class ApiError extends Error {
  constructor(message, status, fields) {
    super(message)
    this.status = status
    this.fields = fields || {}
  }
}

async function handle(res) {
  if (res.status === 204) return null
  const text = await res.text()
  let body = null
  try { body = text ? JSON.parse(text) : null } catch { body = null }

  if (!res.ok) {
    if (res.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    const message = (body && body.message) || `Greška (${res.status})`
    throw new ApiError(message, res.status, body && body.data)
  }
  return body
}

function headers(extra = {}) {
  const h = { ...extra }
  const token = getToken()
  if (token) h.Authorization = `Bearer ${token}`
  return h
}

export const api = {
  get: (path) =>
    fetch(BASE + path, { headers: headers() }).then(handle),

  post: (path, data) =>
    fetch(BASE + path, {
      method: 'POST',
      headers: headers({ 'Content-Type': 'application/json' }),
      body: JSON.stringify(data),
    }).then(handle),

  put: (path, data) =>
    fetch(BASE + path, {
      method: 'PUT',
      headers: headers({ 'Content-Type': 'application/json' }),
      body: JSON.stringify(data),
    }).then(handle),

  patch: (path) =>
    fetch(BASE + path, { method: 'PATCH', headers: headers() }).then(handle),

  delete: (path) =>
    fetch(BASE + path, { method: 'DELETE', headers: headers() }).then(handle),

  upload: (path, formData) =>
    fetch(BASE + path, { method: 'POST', headers: headers(), body: formData }).then(handle),

  download: async (path) => {
    const res = await fetch(BASE + path, { headers: headers() })
    if (!res.ok) throw new ApiError(`Preuzimanje nije uspelo (${res.status})`, res.status)
    return res.blob()
  },
}

export function qs(params) {
  const p = new URLSearchParams()
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') p.append(k, v)
  })
  const s = p.toString()
  return s ? `?${s}` : ''
}

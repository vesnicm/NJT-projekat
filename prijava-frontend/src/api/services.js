import { api, qs } from './client'

export const authApi = {
  login: (data) => api.post('/api/auth/login', data),
  register: (data) => api.post('/api/auth/register', data),
}

export const programiApi = {
  sve: () => api.get('/api/studijski-programi'),
  jedan: (id) => api.get(`/api/studijski-programi/${id}`),
  kreiraj: (data) => api.post('/api/studijski-programi', data),
  izmeni: (id, data) => api.put(`/api/studijski-programi/${id}`, data),
  obrisi: (id) => api.delete(`/api/studijski-programi/${id}`),
}

export const skoleApi = {
  sve: () => api.get('/api/srednje-skole'),
  jedna: (id) => api.get(`/api/srednje-skole/${id}`),
}

export const kandidatiApi = {
  jedan: (id) => api.get(`/api/kandidati/${id}`),
  izmeni: (id, data) => api.put(`/api/kandidati/${id}`, data),
}

export const prijaveApi = {
  sve: () => api.get('/api/prijave'),
  jedna: (id) => api.get(`/api/prijave/${id}`),
  zaKandidata: (kandidatId) => api.get(`/api/prijave/kandidat/${kandidatId}`),
  kreiraj: (kandidatId) => api.post('/api/prijave', { kandidat: { id: kandidatId } }),
  promeniStatus: (id, status, napomena) =>
    api.patch(`/api/prijave/${id}/status${qs({ status, napomena })}`),
  pretraga: (filteri) => api.get(`/api/prijave/pretraga${qs(filteri)}`),
  obrisi: (id) => api.delete(`/api/prijave/${id}`),
}

export const prijavaProgramApi = {
  zaPrijavu: (prijavaId) => api.get(`/api/prijava-program/prijava/${prijavaId}`),
  zaProgram: (programId) => api.get(`/api/prijava-program/program/${programId}`),
  dodaj: (prijavaId, programId) =>
    api.post('/api/prijava-program', { prijava: { id: prijavaId }, studijskiProgram: { id: programId } }),
  obrisi: (id) => api.delete(`/api/prijava-program/${id}`),
}

export const dokumentiApi = {
  zaPrijavu: (prijavaId) => api.get(`/api/dokumenti/prijava/${prijavaId}`),
  upload: (prijavaId, tip, file) => {
    const fd = new FormData()
    fd.append('prijavaId', prijavaId)
    fd.append('tip', tip)
    fd.append('file', file)
    return api.upload('/api/dokumenti/upload', fd)
  },
  preuzmi: (id) => api.download(`/api/dokumenti/${id}/preuzmi`),
  obrisi: (id) => api.delete(`/api/dokumenti/${id}`),
  verifikuj: (id, verifikovan) => api.patch(`/api/dokumenti/${id}/verifikuj?verifikovan=${verifikovan}`),
}

export const STATUSI = {
  PODNETA: 'Podneta',
  POTREBNA_DOPUNA: 'Potrebna dopuna',
  PRIHVACENA: 'Prihvaćena',
  ODBIJENA: 'Odbijena',
}

export const TIPOVI_DOKUMENATA = [
  { key: 'SVEDOCANSTVO_1', label: 'Svedočanstvo I razreda' },
  { key: 'SVEDOCANSTVO_2', label: 'Svedočanstvo II razreda' },
  { key: 'SVEDOCANSTVO_3', label: 'Svedočanstvo III razreda' },
  { key: 'SVEDOCANSTVO_4', label: 'Svedočanstvo IV razreda' },
  { key: 'DIPLOMA', label: 'Diploma o završenoj srednjoj školi' },
  { key: 'IZVOD', label: 'Izvod iz matične knjige rođenih' },
  { key: 'OSTALO', label: 'Ostala dokumenta po konkursu' },
]


export function prosek4(k) {
  if (!k) return 0
  return (Number(k.prosek1Razred) + Number(k.prosek2Razred) + Number(k.prosek3Razred) + Number(k.prosek4Razred)) / 4
}

export function labelTipa(key) {
  const t = TIPOVI_DOKUMENATA.find((x) => x.key === key)
  return t ? t.label : key
}

export function formatDatum(iso) {
  if (!iso) return '—'
  const d = new Date(iso)
  return d.toLocaleDateString('sr-RS') + ' ' + d.toLocaleTimeString('sr-RS', { hour: '2-digit', minute: '2-digit' })
}

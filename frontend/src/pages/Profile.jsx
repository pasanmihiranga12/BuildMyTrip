import { useEffect, useState } from 'react'
import api from '../api/client'

export default function Profile() {
  const [me, setMe] = useState(null)
  const [form, setForm] = useState({ email: '', phone: '' })
  const [msg, setMsg] = useState('')

  useEffect(() => {
    api.get('/auth/me').then(r => {
      setMe(r.data)
      setForm({ email: r.data.email, phone: r.data.phone || '' })
    })
  }, [])

  const save = async (e) => {
    e.preventDefault()
    await api.put('/auth/me', form)
    setMsg('Profile saved')
    setTimeout(() => setMsg(''), 2000)
  }

  if (!me) return <p className="muted">Loading…</p>
  return (
    <div className="auth-wrap">
      <div className="card">
        <h2>Your profile</h2>
        <p>
          <strong>{me.username}</strong> · <span className={`tag ${me.role === 'ADMIN' ? 'admin' : ''}`}>{me.role}</span>
        </p>
        <form className="form" onSubmit={save}>
          <label>Email
            <input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
          </label>
          <label>Phone
            <input placeholder="Phone" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
          </label>
          <button className="btn">Save changes</button>
          {msg && <small style={{ color: 'var(--emerald-deep)' }}>✓ {msg}</small>}
        </form>
      </div>
    </div>
  )
}

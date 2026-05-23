import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function Register() {
  const { register } = useAuth()
  const nav = useNavigate()
  const [form, setForm] = useState({ username: '', password: '', email: '', phone: '' })
  const [err, setErr] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    setErr('')
    try {
      await register(form)
      nav('/')
    } catch (e) {
      setErr(e?.response?.data?.error || 'Registration failed')
    }
  }

  const upd = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  return (
    <div className="auth-wrap">
      <div className="card">
        <h2>Create your account</h2>
        <p className="muted" style={{ marginTop: '-0.5rem' }}>Quick — takes about a minute.</p>
        <form className="form" onSubmit={submit}>
          <input placeholder="Username" value={form.username} onChange={upd('username')} required />
          <input type="email" placeholder="Email" value={form.email} onChange={upd('email')} required />
          <input placeholder="Phone (optional)" value={form.phone} onChange={upd('phone')} />
          <input type="password" placeholder="Password (6+ chars)" value={form.password} onChange={upd('password')} required />
          {err && <div className="error">{err}</div>}
          <button className="btn" type="submit">Create account</button>
          <small className="muted">Already have one? <Link to="/login">Sign in</Link></small>
        </form>
      </div>
    </div>
  )
}

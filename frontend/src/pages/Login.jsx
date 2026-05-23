import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function Login() {
  const { login } = useAuth()
  const nav = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [err, setErr] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    setErr('')
    try {
      const u = await login(username, password)
      nav(u.role === 'ADMIN' ? '/admin' : '/')
    } catch (e) {
      setErr(e?.response?.data?.error || 'Login failed')
    }
  }

  return (
    <div className="auth-wrap">
      <div className="card">
        <h2>Welcome back</h2>
        <p className="muted" style={{ marginTop: '-0.5rem' }}>Sign in to continue planning your trip.</p>
        <form className="form" onSubmit={submit}>
          <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
          <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
          {err && <div className="error">{err}</div>}
          <button className="btn" type="submit">Sign in</button>
          <small className="muted">No account? <Link to="/register">Register</Link></small>
        </form>
      </div>
    </div>
  )
}

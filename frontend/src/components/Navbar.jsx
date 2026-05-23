import { useEffect, useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const nav = useNavigate()
  const loc = useLocation()
  const [open, setOpen] = useState(false)

  useEffect(() => { setOpen(false) }, [loc.pathname])

  const close = () => setOpen(false)

  return (
    <nav className="nav">
      <Link to="/" className="brand" onClick={close}>BillMyTrip</Link>

      <button
        className="nav-toggle"
        aria-label={open ? 'Close menu' : 'Open menu'}
        aria-expanded={open}
        onClick={() => setOpen(o => !o)}
      >
        {open ? '✕' : '☰'}
      </button>

      <div className={`nav-links ${open ? 'open' : ''}`}>
        <Link to="/">Destinations</Link>
        <Link to="/book">Build Package</Link>
        {user && <Link to="/bookings">My Bookings</Link>}
        {user && <Link to="/profile">Profile</Link>}
        {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
        <span className="spacer" />
        {user
          ? <>
              <span className="nav-greet">Hi, {user.username}</span>
              <button
                className="btn secondary"
                onClick={() => { close(); logout(); nav('/') }}
              >Logout</button>
            </>
          : <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>}
      </div>
    </nav>
  )
}

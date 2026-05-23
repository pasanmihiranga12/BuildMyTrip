import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/client'
import { money } from '../lib/format'

export default function MyBookings() {
  const [bookings, setBookings] = useState([])
  const [loading, setLoading] = useState(true)

  const load = () => api.get('/bookings/me').then(r => setBookings(r.data)).finally(() => setLoading(false))

  useEffect(() => { load() }, [])

  const cancel = async (id) => {
    if (!confirm('Cancel this booking?')) return
    await api.post(`/bookings/${id}/cancel`)
    load()
  }

  return (
    <>
      <h1>My bookings</h1>
      {loading ? <p className="muted">Loading…</p>
        : bookings.length === 0
          ? <div className="card">
              <p>No bookings yet.</p>
              <p><Link to="/book">Build your first package →</Link></p>
            </div>
          : bookings.map(b => (
            <div key={b.id} className="card">
              <div className="bk-card">
                <div className="meta">
                  <div>
                    <span className={`tag ${b.status === 'CANCELLED' ? 'cancelled' : ''}`}>{b.status}</span>
                    <strong>Booking #{b.id}</strong>
                  </div>
                  <div style={{ fontSize: '1rem', color: 'var(--ink-soft)' }}>{b.destinationNames}</div>
                  <div className="muted">
                    {b.hotelType} hotel · {b.transportType} · {b.days} days · {b.travelers} travelers
                  </div>
                  {b.activityNames && <div className="muted">Activities: {b.activityNames}</div>}
                  {b.promoCode && <div className="muted">Promo: {b.promoCode}</div>}
                </div>
                <div>
                  <div className="total">{money(b.totalCost)}</div>
                  {b.status === 'CONFIRMED' && (
                    <button className="btn danger" onClick={() => cancel(b.id)} style={{ marginTop: '0.5rem' }}>
                      Cancel
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
    </>
  )
}

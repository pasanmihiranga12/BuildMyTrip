import { useEffect, useState } from 'react'
import api from '../../api/client'
import { money } from '../../lib/format'

export default function AdminDashboard() {
  const [stats, setStats] = useState(null)
  const [revenue, setRevenue] = useState(null)

  useEffect(() => {
    api.get('/admin/reports/bookings').then(r => setStats(r.data))
    api.get('/admin/reports/revenue').then(r => setRevenue(r.data))
  }, [])

  return (
    <div className="grid" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))' }}>
      <div className="card">
        <div className="muted" style={{ fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Total bookings</div>
        <div style={{ fontFamily: 'Playfair Display, serif', fontSize: '2rem', fontWeight: 700 }}>{stats?.totalBookings ?? '—'}</div>
      </div>
      <div className="card">
        <div className="muted" style={{ fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Confirmed</div>
        <div style={{ fontFamily: 'Playfair Display, serif', fontSize: '2rem', fontWeight: 700, color: 'var(--emerald-deep)' }}>{stats?.confirmed ?? '—'}</div>
      </div>
      <div className="card">
        <div className="muted" style={{ fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Cancelled</div>
        <div style={{ fontFamily: 'Playfair Display, serif', fontSize: '2rem', fontWeight: 700, color: 'var(--danger)' }}>{stats?.cancelled ?? '—'}</div>
      </div>
      <div className="card">
        <div className="muted" style={{ fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Revenue (confirmed)</div>
        <div style={{ fontFamily: 'Playfair Display, serif', fontSize: '1.6rem', fontWeight: 700, color: 'var(--emerald-deep)' }}>
          {revenue ? money(revenue.confirmedRevenue) : '—'}
        </div>
      </div>
    </div>
  )
}

import { useEffect, useState } from 'react'
import api from '../../api/client'
import { money } from '../../lib/format'

export default function AdminReports() {
  const [bookings, setBookings] = useState(null)
  const [popular, setPopular] = useState([])
  const [revenue, setRevenue] = useState(null)

  useEffect(() => {
    api.get('/admin/reports/bookings').then(r => setBookings(r.data))
    api.get('/admin/reports/popular-destinations').then(r => setPopular(r.data))
    api.get('/admin/reports/revenue').then(r => setRevenue(r.data))
  }, [])

  return (
    <>
      <div className="card">
        <h3>Bookings</h3>
        {bookings && (
          <p>
            <strong>{bookings.totalBookings}</strong> total ·
            {' '}<span style={{ color: 'var(--emerald-deep)' }}>{bookings.confirmed} confirmed</span> ·
            {' '}<span style={{ color: 'var(--danger)' }}>{bookings.cancelled} cancelled</span>
          </p>
        )}
      </div>

      <div className="card">
        <h3>Popular destinations</h3>
        {popular.length === 0 ? <p className="muted">No bookings yet.</p> : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Destination</th><th>Bookings</th></tr></thead>
              <tbody>{popular.map(p => <tr key={p.destination}><td className="wrap">{p.destination}</td><td>{p.bookings}</td></tr>)}</tbody>
            </table>
          </div>
        )}
      </div>

      <div className="card">
        <h3>Revenue</h3>
        {revenue && (
          <>
            <p>Confirmed: <strong style={{ color: 'var(--emerald-deep)' }}>{money(revenue.confirmedRevenue)}</strong></p>
            <p className="muted">Cancelled (lost): {money(revenue.lostFromCancellations)}</p>
          </>
        )}
      </div>
    </>
  )
}

import { useEffect, useState } from 'react'
import api from '../../api/client'
import { money } from '../../lib/format'

export default function AdminBookings() {
  const [bookings, setBookings] = useState([])

  const load = () => api.get('/admin/bookings').then(r => setBookings(r.data))
  useEffect(() => { load() }, [])

  const cancel = async (id) => {
    if (!confirm('Cancel this booking?')) return
    await api.post(`/bookings/${id}/cancel`)
    load()
  }

  return (
    <div className="table-wrap">
      <table>
        <thead><tr><th>#</th><th>User</th><th className="wrap">Destinations</th><th>Hotel / Transport</th><th>Days / Trav</th><th>Total</th><th>Status</th><th></th></tr></thead>
        <tbody>
          {bookings.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td>
              <td>#{b.userId}</td>
              <td className="wrap" style={{ maxWidth: '260px' }}>{b.destinationNames}</td>
              <td>{b.hotelType} / {b.transportType}</td>
              <td>{b.days} / {b.travelers}</td>
              <td>{money(b.totalCost)}</td>
              <td><span className={`tag ${b.status === 'CANCELLED' ? 'cancelled' : ''}`}>{b.status}</span></td>
              <td>{b.status === 'CONFIRMED' && <button className="btn danger" onClick={() => cancel(b.id)}>Cancel</button>}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

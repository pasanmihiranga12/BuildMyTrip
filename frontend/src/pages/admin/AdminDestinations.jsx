import { useEffect, useState } from 'react'
import api from '../../api/client'
import { money } from '../../lib/format'

const empty = { name: '', location: '', description: '', imageUrl: '', basePrice: '' }

export default function AdminDestinations() {
  const [items, setItems] = useState([])
  const [editing, setEditing] = useState(empty)
  const [editId, setEditId] = useState(null)

  const load = () => api.get('/destinations').then(r => setItems(r.data))
  useEffect(() => { load() }, [])

  const submit = async (e) => {
    e.preventDefault()
    const payload = { ...editing, basePrice: parseFloat(editing.basePrice) }
    if (editId) await api.put(`/destinations/${editId}`, payload)
    else await api.post('/destinations', payload)
    setEditing(empty); setEditId(null); load()
  }

  const startEdit = (d) => { setEditing({ ...d, basePrice: String(d.basePrice) }); setEditId(d.id) }
  const del = async (id) => { if (confirm('Delete this destination?')) { await api.delete(`/destinations/${id}`); load() } }

  return (
    <>
      <div className="card">
        <h3>{editId ? `Edit destination #${editId}` : 'Add a destination'}</h3>
        <form className="form" onSubmit={submit}>
          <input placeholder="Name" value={editing.name} onChange={e => setEditing({ ...editing, name: e.target.value })} required />
          <input placeholder="Location" value={editing.location} onChange={e => setEditing({ ...editing, location: e.target.value })} required />
          <textarea placeholder="Description" value={editing.description} onChange={e => setEditing({ ...editing, description: e.target.value })} rows="3" />
          <input placeholder="Image URL" value={editing.imageUrl} onChange={e => setEditing({ ...editing, imageUrl: e.target.value })} />
          <input type="number" step="0.01" placeholder="Base price (LKR per person per day)" value={editing.basePrice} onChange={e => setEditing({ ...editing, basePrice: e.target.value })} required />
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <button className="btn">{editId ? 'Update' : 'Create'}</button>
            {editId && <button type="button" className="btn secondary" onClick={() => { setEditing(empty); setEditId(null) }}>Cancel</button>}
          </div>
        </form>
      </div>

      <div className="table-wrap">
        <table>
          <thead><tr><th>Name</th><th>Location</th><th>Price / pp / day</th><th></th></tr></thead>
          <tbody>
            {items.map(d => (
              <tr key={d.id}>
                <td>{d.name}</td>
                <td>{d.location}</td>
                <td>{money(d.basePrice)}</td>
                <td>
                  <button className="btn secondary" onClick={() => startEdit(d)}>Edit</button>{' '}
                  <button className="btn danger" onClick={() => del(d.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  )
}

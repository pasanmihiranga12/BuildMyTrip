import { useEffect, useState } from 'react'
import api from '../../api/client'
import { money } from '../../lib/format'

const empty = { name: '', pricePerPerson: '' }

export default function AdminActivities() {
  const [items, setItems] = useState([])
  const [editing, setEditing] = useState(empty)
  const [editId, setEditId] = useState(null)

  const load = () => api.get('/activities').then(r => setItems(r.data))
  useEffect(() => { load() }, [])

  const submit = async (e) => {
    e.preventDefault()
    const payload = { ...editing, pricePerPerson: parseFloat(editing.pricePerPerson) }
    if (editId) await api.put(`/activities/${editId}`, payload)
    else await api.post('/activities', payload)
    setEditing(empty); setEditId(null); load()
  }

  const del = async (id) => { if (confirm('Delete this activity?')) { await api.delete(`/activities/${id}`); load() } }

  return (
    <>
      <div className="card">
        <h3>{editId ? `Edit activity #${editId}` : 'Add an activity'}</h3>
        <form className="form" onSubmit={submit}>
          <input placeholder="Name" value={editing.name} onChange={e => setEditing({ ...editing, name: e.target.value })} required />
          <input type="number" step="0.01" placeholder="Price per person (LKR)" value={editing.pricePerPerson} onChange={e => setEditing({ ...editing, pricePerPerson: e.target.value })} required />
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <button className="btn">{editId ? 'Update' : 'Create'}</button>
            {editId && <button type="button" className="btn secondary" onClick={() => { setEditing(empty); setEditId(null) }}>Cancel</button>}
          </div>
        </form>
      </div>

      <div className="table-wrap">
        <table>
          <thead><tr><th>Name</th><th>Price / person</th><th></th></tr></thead>
          <tbody>
            {items.map(a => (
              <tr key={a.id}>
                <td>{a.name}</td>
                <td>{money(a.pricePerPerson)}</td>
                <td>
                  <button className="btn secondary" onClick={() => { setEditing({ name: a.name, pricePerPerson: String(a.pricePerPerson) }); setEditId(a.id) }}>Edit</button>{' '}
                  <button className="btn danger" onClick={() => del(a.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  )
}

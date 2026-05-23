import { useEffect, useState } from 'react'
import api from '../../api/client'

export default function AdminUsers() {
  const [users, setUsers] = useState([])

  const load = () => api.get('/admin/users').then(r => setUsers(r.data))
  useEffect(() => { load() }, [])

  const del = async (id) => {
    if (!confirm('Delete this user?')) return
    try { await api.delete(`/admin/users/${id}`); load() }
    catch (e) { alert(e?.response?.data?.error || 'Could not delete') }
  }

  return (
    <div className="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Phone</th><th>Role</th><th></th></tr></thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td><td>{u.username}</td><td>{u.email}</td><td>{u.phone}</td><td>{u.role}</td>
              <td>{u.role !== 'ADMIN' && <button className="btn danger" onClick={() => del(u.id)}>Delete</button>}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

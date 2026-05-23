import { NavLink, Outlet } from 'react-router-dom'

export default function AdminLayout() {
  return (
    <>
      <h1>Admin <span style={{ fontFamily: 'Inter, sans-serif', fontWeight: 400, color: 'var(--muted)', fontSize: '0.55em', verticalAlign: 'middle' }}>panel</span></h1>
      <nav className="admin-nav card" style={{ padding: '0.75rem 1rem' }}>
        <NavLink to="/admin" end>Dashboard</NavLink>
        <NavLink to="/admin/destinations">Destinations</NavLink>
        <NavLink to="/admin/activities">Activities</NavLink>
        <NavLink to="/admin/users">Users</NavLink>
        <NavLink to="/admin/bookings">Bookings</NavLink>
        <NavLink to="/admin/reports">Reports</NavLink>
      </nav>
      <Outlet />
    </>
  )
}

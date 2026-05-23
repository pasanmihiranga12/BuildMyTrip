import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/client'
import { money } from '../lib/format'

export default function Home() {
  const [destinations, setDestinations] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/destinations').then(r => setDestinations(r.data)).finally(() => setLoading(false))
  }, [])

  const fallback = (name) => `https://placehold.co/600x400/134e4a/ffffff?text=${encodeURIComponent(name)}`

  return (
    <>
      <section className="hero">
        <h1>Design your perfect Sri Lankan getaway</h1>
        <p>Pick the destinations you love, mix in hotels, transport and activities,
           and build a package priced just for you — in minutes.</p>
        <Link to="/book" className="cta">Start building your package →</Link>
      </section>

      <div className="section-head">
        <h2>Explore destinations</h2>
        <span className="muted">{destinations.length} curated places</span>
      </div>

      {loading
        ? <p className="muted">Loading destinations…</p>
        : <div className="grid">
            {destinations.map(d => (
              <article key={d.id} className="dest-card">
                <div className="img-wrap">
                  <img
                    src={d.imageUrl || fallback(d.name)}
                    alt={d.name}
                    loading="lazy"
                    onError={(e) => { e.target.onerror = null; e.target.src = fallback(d.name) }}
                  />
                  <div className="loc-badge">📍 {d.location}</div>
                </div>
                <div className="body">
                  <h3>{d.name}</h3>
                  <p>{d.description}</p>
                  <div className="price">
                    From {money(d.basePrice)} <small>/ person / day</small>
                  </div>
                </div>
              </article>
            ))}
          </div>}
    </>
  )
}

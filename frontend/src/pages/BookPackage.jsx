import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/client'
import { money } from '../lib/format'

const HOTELS = [
  { v: 'BUDGET', label: 'Budget' },
  { v: 'STANDARD', label: 'Standard' },
  { v: 'LUXURY', label: 'Luxury' }
]
const TRANSPORTS = [
  { v: 'BUS', label: 'Bus' },
  { v: 'TRAIN', label: 'Train' },
  { v: 'FLIGHT', label: 'Flight' }
]

export default function BookPackage() {
  const nav = useNavigate()
  const [destinations, setDestinations] = useState([])
  const [activities, setActivities] = useState([])
  const [form, setForm] = useState({
    destinationIds: [],
    hotelType: 'STANDARD',
    transportType: 'BUS',
    activityIds: [],
    days: 3,
    travelers: 2,
    promoCode: ''
  })
  const [breakdown, setBreakdown] = useState(null)
  const [err, setErr] = useState('')

  useEffect(() => {
    api.get('/destinations').then(r => setDestinations(r.data))
    api.get('/activities').then(r => setActivities(r.data))
  }, [])

  const toggleList = (key, id) => {
    const cur = form[key]
    setForm({ ...form, [key]: cur.includes(id) ? cur.filter(x => x !== id) : [...cur, id] })
    setBreakdown(null)
  }

  const calc = async () => {
    setErr('')
    try {
      const { data } = await api.post('/packages/calculate', form)
      setBreakdown(data)
    } catch (e) {
      setErr(e?.response?.data?.error || 'Could not calculate cost — pick at least one destination.')
    }
  }

  const confirm = async () => {
    setErr('')
    try {
      await api.post('/bookings', form)
      nav('/bookings')
    } catch (e) {
      setErr(e?.response?.data?.error || 'Could not confirm booking')
    }
  }

  return (
    <>
      <h1>Build your package</h1>
      <p className="muted" style={{ marginTop: '-0.5rem', marginBottom: '1.5rem' }}>
        Pick destinations, tune your stay, then preview the cost before you confirm.
      </p>

      <div className="builder-layout">
        <div>
          <div className="card">
            <h3>① Destinations</h3>
            <p className="muted" style={{ marginTop: 0 }}>Tap to add to your trip.</p>
            <div className="checkbox-grid">
              {destinations.map(d => {
                const checked = form.destinationIds.includes(d.id)
                return (
                  <label key={d.id} className={checked ? 'checked' : ''}>
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => toggleList('destinationIds', d.id)}
                    />
                    <span style={{ flex: 1 }}>{d.name}</span>
                    <small className="muted">{money(d.basePrice)}/day</small>
                  </label>
                )
              })}
            </div>
          </div>

          <div className="card">
            <h3>② Travel options</h3>
            <div className="options-row" style={{ marginTop: '0.5rem' }}>
              <label>Hotel type
                <select value={form.hotelType} onChange={e => { setForm({ ...form, hotelType: e.target.value }); setBreakdown(null) }}>
                  {HOTELS.map(h => <option key={h.v} value={h.v}>{h.label}</option>)}
                </select>
              </label>
              <label>Transport
                <select value={form.transportType} onChange={e => { setForm({ ...form, transportType: e.target.value }); setBreakdown(null) }}>
                  {TRANSPORTS.map(t => <option key={t.v} value={t.v}>{t.label}</option>)}
                </select>
              </label>
              <label>Days
                <input type="number" min="1" value={form.days} onChange={e => { setForm({ ...form, days: +e.target.value }); setBreakdown(null) }} />
              </label>
              <label>Travelers
                <input type="number" min="1" value={form.travelers} onChange={e => { setForm({ ...form, travelers: +e.target.value }); setBreakdown(null) }} />
              </label>
            </div>
            <label style={{ marginTop: '0.9rem', display: 'flex', flexDirection: 'column', gap: '0.3rem', fontSize: '0.88rem', color: 'var(--ink-soft)', fontWeight: 500 }}>
              Promo code <small className="muted">try WELCOME10 or SUMMER25</small>
              <input value={form.promoCode} onChange={e => { setForm({ ...form, promoCode: e.target.value }); setBreakdown(null) }} />
            </label>
          </div>

          <div className="card">
            <h3>③ Activities</h3>
            <div className="checkbox-grid">
              {activities.map(a => {
                const checked = form.activityIds.includes(a.id)
                return (
                  <label key={a.id} className={checked ? 'checked' : ''}>
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => toggleList('activityIds', a.id)}
                    />
                    <span style={{ flex: 1 }}>{a.name}</span>
                    <small className="muted">{money(a.pricePerPerson)}/pp</small>
                  </label>
                )
              })}
            </div>
          </div>
        </div>

        <div className="summary">
          <div className="card">
            <h3>Cost summary</h3>
            {!breakdown ? (
              <p className="muted">Pick destinations + options, then calculate to see your bill.</p>
            ) : (
              <div className="breakdown">
                <div className="row"><span>Destinations</span><span className="value">{money(breakdown.destinationsCost)}</span></div>
                <div className="row"><span>Hotel upgrade</span><span className="value">{money(breakdown.hotelCost)}</span></div>
                <div className="row"><span>Transport</span><span className="value">{money(breakdown.transportCost)}</span></div>
                <div className="row"><span>Activities</span><span className="value">{money(breakdown.activitiesCost)}</span></div>
                <hr />
                <div className="row"><span>Subtotal</span><span className="value">{money(breakdown.subtotal)}</span></div>
                {breakdown.promoApplied && (
                  <div className="row promo-line">
                    <span>Promo {breakdown.promoApplied} (−{breakdown.discountPercent}%)</span>
                    <span className="value">−{money(breakdown.discountAmount)}</span>
                  </div>
                )}
                <div className="total">Total {money(breakdown.total)}</div>
              </div>
            )}

            {err && <div className="error" style={{ marginTop: '0.75rem' }}>{err}</div>}

            <div style={{ display: 'flex', gap: '0.5rem', marginTop: '1rem', flexWrap: 'wrap' }}>
              <button className="btn secondary" onClick={calc} disabled={!form.destinationIds.length}>Calculate</button>
              <button className="btn" onClick={confirm} disabled={!breakdown}>Confirm booking</button>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

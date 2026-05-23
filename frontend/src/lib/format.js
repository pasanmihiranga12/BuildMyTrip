const LKR = new Intl.NumberFormat('en-LK', {
  style: 'currency',
  currency: 'LKR',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2
})

export function money(value) {
  const n = Number(value ?? 0)
  if (Number.isNaN(n)) return 'Rs. 0.00'
  return LKR.format(n).replace('LKR', 'Rs.')
}

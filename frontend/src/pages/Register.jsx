import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Button, Field, SelectField } from '../components/UI'

export default function Register() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', email: '', password: '', confirm: '', phone: '', employeeId: '', department: '' })
  const [error, setError] = useState('')
  const [submitted, setSubmitted] = useState(false)
  const update = (key) => (event) => setForm({ ...form, [key]: event.target.value })
  const submit = async (event) => {
    event.preventDefault(); setError('')
    if (!form.name || !form.email || !form.password || !form.employeeId || !form.department) return setError('Please complete the required fields.')
    if (form.password.length < 8) return setError('Password must be at least 8 characters.')
    if (form.password !== form.confirm) return setError('Passwords do not match.')
    try { await register(form); setSubmitted(true) } catch (requestError) { setError(requestError.response?.data?.message || 'Unable to create your account.') }
  }
  if (submitted) return <div className="auth-page"><main className="auth-main"><div className="auth-form-wrap"><span className="eyebrow">Request received</span><h2>Your account is pending approval</h2><p className="auth-subtitle">An administrator will review your details. You will be able to sign in after approval.</p><Button type="button" onClick={() => navigate('/login')} className="full-width">Return to sign in <span>→</span></Button></div></main></div>
  return <div className="auth-page compact-auth"><div className="auth-aside"><div className="brand light"><span className="brand-mark">L</span><span>leave<span>web</span></span></div><div className="auth-quote"><span className="eyebrow">Make room for life</span><h1>A better way to take a break.</h1><p>Set up your workspace account and take control of your time off.</p></div></div><main className="auth-main"><div className="auth-form-wrap register-form"><span className="eyebrow">Get started</span><h2>Create your account</h2><p className="auth-subtitle">Your account will be reviewed before access is granted.</p><form onSubmit={submit}><div className="form-grid"><Field label="Full name *" placeholder="Maya Anderson" value={form.name} onChange={update('name')} /><Field label="Work email *" type="email" placeholder="you@company.com" value={form.email} onChange={update('email')} /><Field label="Password *" type="password" placeholder="At least 8 characters" value={form.password} onChange={update('password')} /><Field label="Confirm password *" type="password" placeholder="Repeat password" value={form.confirm} onChange={update('confirm')} /><Field label="Phone number" placeholder="+1 (555) 000-0000" value={form.phone} onChange={update('phone')} /><Field label="Employee ID *" placeholder="EMP-0000" value={form.employeeId} onChange={update('employeeId')} /><SelectField label="Department *" value={form.department} onChange={update('department')} required><option value="">Select department</option>{['Engineering', 'Human Resources', 'Finance', 'Marketing', 'Operations', 'Sales', 'IT', 'Administration'].map((department) => <option key={department} value={department}>{department}</option>)}</SelectField></div>{error && <div className="alert error">{error}</div>}<Button type="submit" className="full-width">Submit for approval <span>→</span></Button></form><p className="switch-auth">Already have an account? <Link to="/login">Sign in</Link></p></div></main></div>
}

import { Link } from 'react-router-dom'

export function Button({ children, variant = 'primary', className = '', ...props }) { return <button className={`button button-${variant} ${className}`} {...props}>{children}</button> }
export function Badge({ children }) { return <span className={`badge badge-${String(children).toLowerCase()}`}>{children}</span> }
export function Card({ children, className = '' }) { return <section className={`card ${className}`}>{children}</section> }
export function EmptyState({ title = 'Nothing here yet', text = 'There is no information to display.' }) { return <div className="empty-state"><span className="empty-icon">○</span><strong>{title}</strong><p>{text}</p></div> }
export function PageHeader({ eyebrow, title, description, action }) { return <div className="page-header"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1>{description && <p>{description}</p>}</div>{action}</div> }
export function StatCard({ label, value, detail, tone = 'teal', icon }) { return <Card className="stat-card"><div className={`stat-icon ${tone}`}>{icon}</div><div><span className="muted">{label}</span><strong>{value}</strong><small>{detail}</small></div></Card> }
export function StatusBadge({ status }) { return <Badge>{status}</Badge> }
export function LoadingState() { return <div className="loading-state">Loading your workspace...</div> }
export function Field({ label, error, ...props }) { return <label className="field"><span>{label}</span><input {...props} />{error && <small className="field-error">{error}</small>}</label> }
export function SelectField({ label, children, ...props }) { return <label className="field"><span>{label}</span><select {...props}>{children}</select></label> }
export function LinkButton({ to, children, variant = 'secondary' }) { return <Link className={`button button-${variant}`} to={to}>{children}</Link> }
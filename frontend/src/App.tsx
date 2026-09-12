import './App.css'
import { useAuthStore } from './shared/store/auth-store'
import { useUiStore } from './shared/store/ui-store'

function App() {
  const { user, isAuthenticated, setTokens, setUser, clearSession } = useAuthStore()
  const {
    isProjectCreateOpen,
    isIssueCreateOpen,
    isIssueDetailsOpen,
    selectedIssueId,
    setProjectCreateOpen,
    setIssueCreateOpen,
    openIssueDetails,
    closeIssueDetails,
  } = useUiStore()

  const seedDemoSession = () => {
    setTokens({
      accessToken: 'demo-access-token',
      refreshToken: 'demo-refresh-token',
    })
    setUser({
      id: 1,
      email: 'demo@taskforge.local',
      name: 'Demo User',
      roles: 'ROLE_USER',
    })
  }

  return (
    <main className="app-shell">
      <section className="app-header">
        <div>
          <p className="eyebrow">Frontend scaffold</p>
          <h1>TaskForge state layer</h1>
          <p className="description">
            Zustand is installed and wired for auth and UI state. Keep API data in
            TanStack Query later.
          </p>
        </div>
        <div className="header-actions">
          <button type="button" className="secondary-button" onClick={seedDemoSession}>
            Seed auth state
          </button>
          <button type="button" className="primary-button" onClick={clearSession}>
            Clear session
          </button>
        </div>
      </section>

      <section className="panel-grid">
        <article className="panel">
          <div className="panel-heading">
            <h2>Auth store</h2>
            <span className={isAuthenticated ? 'status status-active' : 'status'}>
              {isAuthenticated ? 'Authenticated' : 'Guest'}
            </span>
          </div>
          <dl className="details-list">
            <div>
              <dt>User</dt>
              <dd>{user?.name ?? 'Not loaded'}</dd>
            </div>
            <div>
              <dt>Email</dt>
              <dd>{user?.email ?? 'Not loaded'}</dd>
            </div>
            <div>
              <dt>Role</dt>
              <dd>{user?.roles ?? 'Not loaded'}</dd>
            </div>
          </dl>
        </article>

        <article className="panel">
          <div className="panel-heading">
            <h2>UI store</h2>
            <span className="status">Shared client state</span>
          </div>
          <div className="button-row">
            <button
              type="button"
              className="secondary-button"
              onClick={() => setProjectCreateOpen(!isProjectCreateOpen)}
            >
              Toggle project modal
            </button>
            <button
              type="button"
              className="secondary-button"
              onClick={() => setIssueCreateOpen(!isIssueCreateOpen)}
            >
              Toggle issue modal
            </button>
            <button
              type="button"
              className="secondary-button"
              onClick={() => openIssueDetails(42)}
            >
              Open issue drawer
            </button>
            <button type="button" className="secondary-button" onClick={closeIssueDetails}>
              Close issue drawer
            </button>
          </div>
          <dl className="details-list">
            <div>
              <dt>Create project</dt>
              <dd>{isProjectCreateOpen ? 'Open' : 'Closed'}</dd>
            </div>
            <div>
              <dt>Create issue</dt>
              <dd>{isIssueCreateOpen ? 'Open' : 'Closed'}</dd>
            </div>
            <div>
              <dt>Issue details</dt>
              <dd>{isIssueDetailsOpen ? `Open for #${selectedIssueId}` : 'Closed'}</dd>
            </div>
          </dl>
        </article>
      </section>
    </main>
  )
}

export default App

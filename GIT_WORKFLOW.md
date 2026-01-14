# Git Workflow Guide

## Branch Strategy

- `main` — stable, production-ready code
- `develop` — integration branch for features
- `feature/*` — individual feature branches
- `hotfix/*` — urgent fixes

## Commit Message Convention

Follow **Conventional Commits**:

```
<type>(<scope>): <description>

[optional body]
[optional footer]
```

### Types
| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation |
| `style` | Formatting (no code change) |
| `refactor` | Code restructuring |
| `test` | Adding tests |
| `chore` | Maintenance tasks |
| `ci` | CI/CD changes |
| `perf` | Performance improvement |

### Examples
```
feat(backend): add WebSocket STOMP configuration
fix(agent): handle reconnection on WebSocket disconnect
docs: update README with API reference
chore(docker): optimize multi-stage build caching
```

## Suggested Development Workflow

1. **Project Initialization** — Scaffold project structure, .gitignore
2. **Backend Foundation** — Entities, DTOs, Spring Boot setup
3. **Database Integration** — JPA configuration, schema
4. **REST API** — Controllers, services, repositories
5. **WebSocket Streaming** — STOMP config, metric broadcasting
6. **Alert System** — Threshold detection, alert persistence
7. **Monitoring Agent** — OSHI integration, WebSocket client
8. **Frontend Dashboard** — React + TailwindCSS layout
9. **Real-Time Charts** — Recharts integration, WebSocket subscriptions
10. **Server Detail Page** — Individual server view with charts
11. **Dockerization** — Dockerfiles, Docker Compose
12. **Documentation** — README, API docs, workflow guide

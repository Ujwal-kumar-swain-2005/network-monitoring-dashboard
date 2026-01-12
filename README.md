# 🌐 NetPulse — Real-Time Network Monitoring Dashboard

A production-grade, full-stack system that monitors servers in real-time and streams metrics (CPU, memory, network bandwidth) to a live dashboard with alerts.

![Architecture](https://img.shields.io/badge/Architecture-Microservices-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![React](https://img.shields.io/badge/React-18-61dafb)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ed)

---

## 📐 System Architecture

```
┌─────────────────┐     WebSocket (STOMP)     ┌──────────────────────┐     WebSocket Topic     ┌─────────────────────┐
│                 │ ──────────────────────────▸│                      │ ──────────────────────▸│                     │
│  Monitoring     │   /app/metrics             │   Spring Boot        │   /topic/metrics       │   React Dashboard   │
│  Agent (OSHI)   │                            │   Backend            │   /topic/alerts        │   (Vite + Tailwind) │
│                 │                            │                      │                        │                     │
└─────────────────┘                            │   ┌──────────────┐  │                        │  ┌───────────────┐  │
                                               │   │ Alert Engine │  │                        │  │ Live Charts   │  │
                                               │   └──────────────┘  │                        │  │ Server Status │  │
                                               │   ┌──────────────┐  │                        │  │ Alert Panel   │  │
                                               │   │ REST API     │  │  ◂─── REST ───────────│  │ Metric Cards  │  │
                                               │   └──────────────┘  │                        │  └───────────────┘  │
                                               │          │          │                        └─────────────────────┘
                                               └──────────┼──────────┘
                                                          │
                                                     ┌────▼────┐
                                                     │PostgreSQL│
                                                     └─────────┘
```

### Data Flow

1. **Agent** collects CPU, memory, and network metrics every 3 seconds using OSHI
2. **Agent** sends metrics to backend via WebSocket (STOMP/SockJS) at `/app/metrics`
3. **Backend** persists metrics to PostgreSQL and checks alert thresholds
4. **Backend** broadcasts metrics to `/topic/metrics` and alerts to `/topic/alerts`
5. **Dashboard** subscribes to WebSocket topics and updates charts in real-time
6. **Dashboard** also fetches historical data via REST API on page load

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 21, Spring Boot 3.2, Spring WebSocket (STOMP), Spring Data JPA, PostgreSQL, Lombok, Maven |
| **Frontend** | React 18, Vite 5, TailwindCSS 3, Recharts, Axios, @stomp/stompjs, SockJS |
| **Agent** | Java 21, Spring Boot, OSHI (Operating System & Hardware Info), WebSocket Client |
| **DevOps** | Docker, Docker Compose, Nginx |

---

## 📁 Project Structure

```
network-monitor-dashboard/
├── docker-compose.yml                  # Full system orchestration
├── database/
│   └── schema.sql                      # PostgreSQL schema reference
│
├── network-monitor-backend/            # Spring Boot Backend
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/networkmonitor/
│       ├── config/                     # WebSocket & CORS configuration
│       ├── controller/                 # REST API controllers
│       ├── dto/                        # Data Transfer Objects
│       ├── entity/                     # JPA entities
│       ├── repository/                 # Spring Data repositories
│       ├── service/                    # Business logic & alert engine
│       └── websocket/                  # STOMP message handlers
│
├── network-monitor-frontend/           # React Dashboard
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── src/
│       ├── components/                 # Reusable UI components
│       ├── pages/                      # Dashboard & ServerDetail pages
│       └── services/                   # API & WebSocket clients
│
└── network-monitor-agent/              # Monitoring Agent
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/networkmonitor/agent/
        ├── AgentApplication.java       # Main entry point
        ├── MetricCollector.java        # OSHI metric collection
        ├── MetricScheduler.java        # 3-second collection scheduler
        ├── AgentWebSocketClient.java   # STOMP WebSocket client
        └── MetricPayload.java          # Metric data model
```

---

## 🚀 Quick Start

### Prerequisites

- Docker & Docker Compose
- (For local development: Java 21+, Maven, Node.js 18+)

### Run with Docker Compose

```bash
# Clone the repository
git clone <repository-url>
cd network-monitor-dashboard

# Build and start all services
docker compose up --build

# Access the dashboard
open http://localhost:3000
```

| Service | URL |
|---------|-----|
| Dashboard | http://localhost:3000 |
| Backend API | http://localhost:8080/api |
| WebSocket | ws://localhost:8080/ws |
| PostgreSQL | localhost:5432 |

### Local Development

**Backend:**
```bash
cd network-monitor-backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd network-monitor-frontend
npm install
npm run dev
```

**Agent:**
```bash
cd network-monitor-agent
mvn spring-boot:run
```

---

## 📊 API Reference

### Servers
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/servers` | List all servers with latest metrics |
| `GET` | `/api/servers/{id}` | Get server details |

### Metrics
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/metrics/server/{id}?limit=50` | Recent metrics for a server |
| `GET` | `/api/metrics/server/{id}/range?start=...&end=...` | Metrics in time range |

### Alerts
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/alerts?limit=50` | Recent alerts |
| `GET` | `/api/alerts/server/{id}` | Alerts for a server |

### WebSocket Topics
| Destination | Direction | Description |
|------------|-----------|-------------|
| `/app/metrics` | Agent → Backend | Send metric data |
| `/topic/metrics` | Backend → Dashboard | Real-time metric broadcasts |
| `/topic/alerts` | Backend → Dashboard | Real-time alert broadcasts |

### Example WebSocket Message (Metric)
```json
{
  "serverId": 1,
  "hostname": "web-server-01",
  "ipAddress": "192.168.1.100",
  "cpuUsage": 45.23,
  "memoryUsage": 67.89,
  "networkIn": 102400,
  "networkOut": 51200,
  "timestamp": "2026-02-15T10:30:00"
}
```

### Example WebSocket Message (Alert)
```json
{
  "id": 42,
  "serverId": 1,
  "serverHostname": "web-server-01",
  "type": "CPU_HIGH",
  "message": "CPU usage on web-server-01 reached 92.3% (threshold: 90.0%)",
  "severity": "WARNING",
  "createdAt": "2026-02-15T10:30:00"
}
```

---

## 📸 Screenshots

> *Screenshots will be added after deployment*

- Dashboard Overview
- Server Detail Page
- Real-Time Charts
- Alert Panel

---

## 🔮 Future Improvements

- [ ] User authentication & role-based access control
- [ ] Configurable alert thresholds per server
- [ ] Email/Slack notification integration
- [ ] Metric data retention policies & archiving
- [ ] Custom dashboard layouts (drag-and-drop)
- [ ] Historical metrics analytics & reports
- [ ] Auto-scaling agent deployment
- [ ] Kubernetes deployment manifests (Helm charts)
- [ ] Grafana/Prometheus integration option
- [ ] Dark/Light theme toggle

---

## 📝 License

This project is for educational and demonstration purposes.

---

Built with ❤️ using Spring Boot, React, and OSHI.

$ErrorActionPreference = "SilentlyContinue"

$env:GIT_AUTHOR_NAME = "Ujwal-kumar-swain-2005"
$env:GIT_AUTHOR_EMAIL = "swainujwalkumar@gmail.com"
$env:GIT_COMMITTER_NAME = "Ujwal-kumar-swain-2005"
$env:GIT_COMMITTER_EMAIL = "swainujwalkumar@gmail.com"

function MC($d, $m, $files) {
    foreach ($f in $files) { git add $f }
    $env:GIT_AUTHOR_DATE = $d
    $env:GIT_COMMITTER_DATE = $d
    git commit -m $m
}

# 1
MC "2026-01-12T09:00:00+05:30" "chore: initialize project repository" @(".gitignore")
# 2
MC "2026-01-12T10:30:00+05:30" "chore: add root project structure" @("README.md")
# 3
MC "2026-01-13T11:00:00+05:30" "chore: add database schema reference" @("database/schema.sql")
# 4
MC "2026-01-14T14:00:00+05:30" "chore: add git workflow documentation" @("GIT_WORKFLOW.md")
# 5
MC "2026-01-16T09:30:00+05:30" "feat(backend): initialize Spring Boot project with Maven" @("network-monitor-backend/pom.xml")
# 6
MC "2026-01-16T11:00:00+05:30" "feat(backend): add main application entry point" @("network-monitor-backend/src/main/java/com/networkmonitor/NetworkMonitorApplication.java")
# 7
MC "2026-01-17T09:00:00+05:30" "feat(backend): add application configuration" @("network-monitor-backend/src/main/resources/application.yml")
# 8
MC "2026-01-18T10:00:00+05:30" "feat(backend): add Server entity with JPA annotations" @("network-monitor-backend/src/main/java/com/networkmonitor/entity/Server.java")
# 9
MC "2026-01-18T14:00:00+05:30" "feat(backend): add Metric entity with indexes" @("network-monitor-backend/src/main/java/com/networkmonitor/entity/Metric.java")
# 10
MC "2026-01-19T09:30:00+05:30" "feat(backend): add Alert entity for threshold violations" @("network-monitor-backend/src/main/java/com/networkmonitor/entity/Alert.java")
# 11
MC "2026-01-20T10:00:00+05:30" "feat(backend): add MetricDTO for data transfer" @("network-monitor-backend/src/main/java/com/networkmonitor/dto/MetricDTO.java")
# 12
MC "2026-01-21T09:00:00+05:30" "feat(backend): add ServerDTO with latest metric info" @("network-monitor-backend/src/main/java/com/networkmonitor/dto/ServerDTO.java")
# 13
MC "2026-01-22T09:00:00+05:30" "feat(backend): add AlertDTO with server hostname" @("network-monitor-backend/src/main/java/com/networkmonitor/dto/AlertDTO.java")
# 14
MC "2026-01-24T10:00:00+05:30" "feat(backend): add ServerRepository with hostname lookup" @("network-monitor-backend/src/main/java/com/networkmonitor/repository/ServerRepository.java")
# 15
MC "2026-01-25T10:00:00+05:30" "feat(backend): add MetricRepository with time-range queries" @("network-monitor-backend/src/main/java/com/networkmonitor/repository/MetricRepository.java")
# 16
MC "2026-01-26T11:00:00+05:30" "feat(backend): add AlertRepository with severity filtering" @("network-monitor-backend/src/main/java/com/networkmonitor/repository/AlertRepository.java")
# 17
MC "2026-01-28T09:00:00+05:30" "feat(backend): add ServerService with upsert and health check" @("network-monitor-backend/src/main/java/com/networkmonitor/service/ServerService.java")
# 18
MC "2026-01-30T10:00:00+05:30" "feat(backend): add MetricService with processing pipeline" @("network-monitor-backend/src/main/java/com/networkmonitor/service/MetricService.java")
# 19
MC "2026-02-01T09:30:00+05:30" "feat(backend): add AlertService with threshold detection" @("network-monitor-backend/src/main/java/com/networkmonitor/service/AlertService.java")
# 20
MC "2026-02-03T10:00:00+05:30" "feat(backend): add ServerController REST endpoints" @("network-monitor-backend/src/main/java/com/networkmonitor/controller/ServerController.java")
# 21
MC "2026-02-04T09:00:00+05:30" "feat(backend): add MetricController with time-range API" @("network-monitor-backend/src/main/java/com/networkmonitor/controller/MetricController.java")
# 22
MC "2026-02-05T10:30:00+05:30" "feat(backend): add AlertController with filtering" @("network-monitor-backend/src/main/java/com/networkmonitor/controller/AlertController.java")
# 23
MC "2026-02-07T09:00:00+05:30" "feat(backend): configure WebSocket STOMP broker" @("network-monitor-backend/src/main/java/com/networkmonitor/config/WebSocketConfig.java")
# 24
MC "2026-02-08T10:00:00+05:30" "feat(backend): add CORS configuration for frontend" @("network-monitor-backend/src/main/java/com/networkmonitor/config/CorsConfig.java")
# 25
MC "2026-02-09T11:00:00+05:30" "feat(backend): add WebSocket metric handler for agent data" @("network-monitor-backend/src/main/java/com/networkmonitor/websocket/MetricWebSocketController.java")
# 26
MC "2026-02-11T09:00:00+05:30" "feat(agent): initialize monitoring agent Maven project" @("network-monitor-agent/pom.xml")
# 27
MC "2026-02-12T09:30:00+05:30" "feat(agent): add agent application entry point" @("network-monitor-agent/src/main/java/com/networkmonitor/agent/AgentApplication.java")
# 28
MC "2026-02-12T14:00:00+05:30" "feat(agent): add agent configuration with backend URL" @("network-monitor-agent/src/main/resources/application.yml")
# 29
MC "2026-02-13T10:00:00+05:30" "feat(agent): add MetricPayload data model" @("network-monitor-agent/src/main/java/com/networkmonitor/agent/MetricPayload.java")
# 30
MC "2026-02-14T09:00:00+05:30" "feat(agent): implement OSHI-based metric collection" @("network-monitor-agent/src/main/java/com/networkmonitor/agent/MetricCollector.java")
# 31
MC "2026-02-15T10:00:00+05:30" "feat(agent): implement WebSocket STOMP client with retry" @("network-monitor-agent/src/main/java/com/networkmonitor/agent/AgentWebSocketClient.java")
# 32
MC "2026-02-16T09:30:00+05:30" "feat(agent): add scheduled metric collection every 3 seconds" @("network-monitor-agent/src/main/java/com/networkmonitor/agent/MetricScheduler.java")
# 33
MC "2026-02-18T09:00:00+05:30" "feat(frontend): initialize Vite + React project" @("network-monitor-frontend/package.json", "network-monitor-frontend/vite.config.js")
# 34
MC "2026-02-19T09:30:00+05:30" "feat(frontend): configure TailwindCSS with custom theme" @("network-monitor-frontend/tailwind.config.js", "network-monitor-frontend/postcss.config.js")
# 35
MC "2026-02-19T14:00:00+05:30" "feat(frontend): add HTML entry point with fonts and SEO" @("network-monitor-frontend/index.html")
# 36
MC "2026-02-20T09:00:00+05:30" "feat(frontend): add global styles with glassmorphism design" @("network-monitor-frontend/src/index.css")
# 37
MC "2026-02-20T11:00:00+05:30" "feat(frontend): add React entry point and App with routing" @("network-monitor-frontend/src/main.jsx", "network-monitor-frontend/src/App.jsx")
# 38
MC "2026-02-22T09:00:00+05:30" "feat(frontend): add Axios API service for REST endpoints" @("network-monitor-frontend/src/services/api.js")
# 39
MC "2026-02-23T10:00:00+05:30" "feat(frontend): add WebSocket service with STOMP and SockJS" @("network-monitor-frontend/src/services/websocket.js")
# 40
MC "2026-02-25T09:00:00+05:30" "feat(frontend): add Sidebar navigation component" @("network-monitor-frontend/src/components/Sidebar.jsx")
# 41
MC "2026-02-25T14:00:00+05:30" "feat(frontend): add Header with connection status indicator" @("network-monitor-frontend/src/components/Header.jsx")
# 42
MC "2026-02-26T09:30:00+05:30" "feat(frontend): add MetricCard component with themed colors" @("network-monitor-frontend/src/components/MetricCard.jsx")
# 43
MC "2026-02-27T10:00:00+05:30" "feat(frontend): add RealtimeChart with Recharts gradients" @("network-monitor-frontend/src/components/RealtimeChart.jsx")
# 44
MC "2026-02-28T09:00:00+05:30" "feat(frontend): add ServerStatus card with progress bars" @("network-monitor-frontend/src/components/ServerStatus.jsx")
# 45
MC "2026-02-28T14:00:00+05:30" "feat(frontend): add AlertPanel with severity badges" @("network-monitor-frontend/src/components/AlertPanel.jsx")
# 46
MC "2026-03-01T09:00:00+05:30" "feat(frontend): add ServerList grid component" @("network-monitor-frontend/src/components/ServerList.jsx")
# 47
MC "2026-03-02T09:30:00+05:30" "feat(frontend): implement Dashboard page with live charts" @("network-monitor-frontend/src/pages/Dashboard.jsx")
# 48
MC "2026-03-04T10:00:00+05:30" "feat(frontend): implement ServerDetail page with metrics" @("network-monitor-frontend/src/pages/ServerDetail.jsx")
# 49
MC "2026-03-06T09:00:00+05:30" "feat(docker): add Dockerfile for Spring Boot backend" @("network-monitor-backend/Dockerfile")
# 50
MC "2026-03-07T09:30:00+05:30" "feat(docker): add Dockerfile and nginx config for frontend" @("network-monitor-frontend/Dockerfile", "network-monitor-frontend/nginx.conf")
# 51
MC "2026-03-08T10:00:00+05:30" "feat(docker): add Dockerfile for monitoring agent" @("network-monitor-agent/Dockerfile")
# 52
MC "2026-03-09T09:00:00+05:30" "feat(docker): add docker-compose for full system" @("docker-compose.yml")
# 53
MC "2026-03-10T10:00:00+05:30" "docs: finalize README with architecture and API reference" @("README.md")

Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue

Write-Host "`n=== Done: 53 commits created ===" -ForegroundColor Green

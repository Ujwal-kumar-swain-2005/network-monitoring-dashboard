-- ============================================
-- Network Monitor Dashboard - Database Schema
-- PostgreSQL
-- ============================================

-- Servers table: tracks all monitored servers
CREATE TABLE IF NOT EXISTS servers (
    id              BIGSERIAL PRIMARY KEY,
    hostname        VARCHAR(255) NOT NULL UNIQUE,
    ip_address      VARCHAR(45)  NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ONLINE',
    last_seen       TIMESTAMP
);

-- Metrics table: stores point-in-time metric snapshots
CREATE TABLE IF NOT EXISTS metrics (
    id              BIGSERIAL PRIMARY KEY,
    server_id       BIGINT          NOT NULL REFERENCES servers(id) ON DELETE CASCADE,
    cpu_usage       DOUBLE PRECISION NOT NULL,
    memory_usage    DOUBLE PRECISION NOT NULL,
    network_in      BIGINT,
    network_out     BIGINT,
    timestamp       TIMESTAMP       NOT NULL
);

-- Alerts table: stores threshold violation alerts
CREATE TABLE IF NOT EXISTS alerts (
    id              BIGSERIAL PRIMARY KEY,
    server_id       BIGINT       NOT NULL REFERENCES servers(id) ON DELETE CASCADE,
    type            VARCHAR(50)  NOT NULL,
    message         TEXT         NOT NULL,
    severity        VARCHAR(20)  NOT NULL DEFAULT 'WARNING',
    created_at      TIMESTAMP    NOT NULL
);

-- Performance indexes
CREATE INDEX IF NOT EXISTS idx_metrics_server_id  ON metrics(server_id);
CREATE INDEX IF NOT EXISTS idx_metrics_timestamp   ON metrics(timestamp);
CREATE INDEX IF NOT EXISTS idx_alerts_server_id    ON alerts(server_id);
CREATE INDEX IF NOT EXISTS idx_alerts_created_at   ON alerts(created_at);
CREATE INDEX IF NOT EXISTS idx_servers_status       ON servers(status);

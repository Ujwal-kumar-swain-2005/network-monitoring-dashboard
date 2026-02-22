import axios from 'axios';

/**
 * Base API URL — proxied through Vite in dev, direct in production.
 */
const API_BASE = import.meta.env.VITE_API_URL || '/api';

const api = axios.create({
  baseURL: API_BASE,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ===== Server Endpoints =====

export const fetchServers = () => api.get('/servers').then((res) => res.data);

export const fetchServerById = (id) => api.get(`/servers/${id}`).then((res) => res.data);

// ===== Metric Endpoints =====

export const fetchMetrics = (serverId, limit = 50) =>
  api.get(`/metrics/server/${serverId}`, { params: { limit } }).then((res) => res.data);

export const fetchMetricsByRange = (serverId, start, end) =>
  api.get(`/metrics/server/${serverId}/range`, { params: { start, end } }).then((res) => res.data);

// ===== Alert Endpoints =====

export const fetchAlerts = (limit = 50) =>
  api.get('/alerts', { params: { limit } }).then((res) => res.data);

export const fetchAlertsByServer = (serverId) =>
  api.get(`/alerts/server/${serverId}`).then((res) => res.data);

export default api;

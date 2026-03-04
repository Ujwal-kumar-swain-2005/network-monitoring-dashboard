import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Server,
  Cpu,
  MemoryStick,
  Network,
  Activity,
  Clock,
} from 'lucide-react';
import MetricCard from '../components/MetricCard';
import RealtimeChart from '../components/RealtimeChart';
import AlertPanel from '../components/AlertPanel';
import { fetchServerById, fetchMetrics, fetchAlertsByServer } from '../services/api';
import { connectWebSocket, disconnectWebSocket } from '../services/websocket';

const MAX_CHART_POINTS = 60;

/**
 * Server detail page showing individual server metrics,
 * real-time charts for CPU, Memory, and Network, along with alerts.
 */
function ServerDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [server, setServer] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const [cpuMemData, setCpuMemData] = useState([]);
  const [networkData, setNetworkData] = useState([]);
  const [latestMetric, setLatestMetric] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // Load initial data
  useEffect(() => {
    async function loadData() {
      try {
        const [serverData, metricsData, alertsData] = await Promise.all([
          fetchServerById(id),
          fetchMetrics(id, 60),
          fetchAlertsByServer(id),
        ]);

        setServer(serverData);
        setAlerts(alertsData);

        // Build initial chart data from historical metrics (reversed since they come desc)
        const reversed = [...metricsData].reverse();
        const initialCpuMem = reversed.map((m) => ({
          time: new Date(m.timestamp).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false,
          }),
          cpu: m.cpuUsage,
          memory: m.memoryUsage,
        }));
        setCpuMemData(initialCpuMem);

        const initialNetwork = reversed.map((m) => ({
          time: new Date(m.timestamp).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false,
          }),
          networkIn: Math.round((m.networkIn || 0) / 1024),
          networkOut: Math.round((m.networkOut || 0) / 1024),
        }));
        setNetworkData(initialNetwork);

        if (reversed.length > 0) {
          setLatestMetric(reversed[reversed.length - 1]);
        }
      } catch (err) {
        console.error('Failed to load server data:', err);
      } finally {
        setIsLoading(false);
      }
    }
    loadData();
  }, [id]);

  // Handle incoming real-time metric for this server
  const handleMetric = useCallback(
    (metric) => {
      if (String(metric.serverId) !== String(id)) return;

      setLatestMetric(metric);

      // Update server info
      setServer((prev) =>
        prev
          ? { ...prev, cpuUsage: metric.cpuUsage, memoryUsage: metric.memoryUsage, status: 'ONLINE' }
          : prev
      );

      const timeStr = new Date(metric.timestamp || Date.now()).toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
      });

      // Update CPU/Memory chart
      setCpuMemData((prev) => {
        const updated = [
          ...prev,
          { time: timeStr, cpu: metric.cpuUsage, memory: metric.memoryUsage },
        ];
        return updated.length > MAX_CHART_POINTS ? updated.slice(-MAX_CHART_POINTS) : updated;
      });

      // Update Network chart
      setNetworkData((prev) => {
        const updated = [
          ...prev,
          {
            time: timeStr,
            networkIn: Math.round((metric.networkIn || 0) / 1024),
            networkOut: Math.round((metric.networkOut || 0) / 1024),
          },
        ];
        return updated.length > MAX_CHART_POINTS ? updated.slice(-MAX_CHART_POINTS) : updated;
      });
    },
    [id]
  );

  // Handle incoming alert for this server
  const handleAlert = useCallback(
    (alert) => {
      if (String(alert.serverId) !== String(id)) return;
      setAlerts((prev) => [alert, ...prev].slice(0, 50));
    },
    [id]
  );

  // WebSocket connection
  useEffect(() => {
    connectWebSocket(handleMetric, handleAlert);
    return () => disconnectWebSocket();
  }, [handleMetric, handleAlert]);

  // Chart configurations
  const cpuMemLines = [
    { key: 'cpu', name: 'CPU Usage', color: '#22d3ee' },
    { key: 'memory', name: 'Memory Usage', color: '#a78bfa' },
  ];

  const networkLines = [
    { key: 'networkIn', name: 'Network In', color: '#34d399' },
    { key: 'networkOut', name: 'Network Out', color: '#fb923c' },
  ];

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="flex flex-col items-center gap-3">
          <Activity className="w-8 h-8 text-brand-400 animate-spin" />
          <p className="text-surface-400 text-sm">Loading server details...</p>
        </div>
      </div>
    );
  }

  if (!server) {
    return (
      <div className="flex flex-col items-center justify-center h-full text-surface-400">
        <Server className="w-12 h-12 mb-4 opacity-30" />
        <p className="text-lg">Server not found</p>
        <button
          onClick={() => navigate('/')}
          className="mt-4 text-brand-400 hover:text-brand-300 text-sm transition-colors"
        >
          ← Back to Dashboard
        </button>
      </div>
    );
  }

  const isOnline = server.status === 'ONLINE';

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Back Button + Server Header */}
      <div className="flex items-center gap-4">
        <button
          onClick={() => navigate('/')}
          className="p-2 rounded-xl bg-surface-800/80 border border-surface-700/50 text-surface-400 hover:text-surface-200 hover:border-surface-600 transition-all"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div className="flex items-center gap-3">
          <div className={`p-2.5 rounded-xl ${isOnline ? 'bg-emerald-500/15' : 'bg-red-500/15'}`}>
            <Server className={`w-6 h-6 ${isOnline ? 'text-emerald-400' : 'text-red-400'}`} />
          </div>
          <div>
            <h1 className="text-xl font-bold text-surface-100">{server.hostname}</h1>
            <div className="flex items-center gap-3 mt-0.5">
              <span className="text-sm text-surface-500 font-mono">{server.ipAddress}</span>
              <span className={isOnline ? 'status-online' : 'status-offline'}>
                <span className={`w-1.5 h-1.5 rounded-full mr-1.5 ${isOnline ? 'bg-emerald-400 animate-pulse-dot' : 'bg-red-400'}`} />
                {server.status}
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* Metric Overview Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <MetricCard
          title="CPU Usage"
          value={`${(latestMetric?.cpuUsage || server.cpuUsage || 0).toFixed(1)}%`}
          icon={Cpu}
          color={(latestMetric?.cpuUsage || 0) > 80 ? 'red' : (latestMetric?.cpuUsage || 0) > 60 ? 'amber' : 'cyan'}
        />
        <MetricCard
          title="Memory Usage"
          value={`${(latestMetric?.memoryUsage || server.memoryUsage || 0).toFixed(1)}%`}
          icon={MemoryStick}
          color={(latestMetric?.memoryUsage || 0) > 80 ? 'red' : (latestMetric?.memoryUsage || 0) > 60 ? 'amber' : 'purple'}
        />
        <MetricCard
          title="Network In"
          value={`${Math.round((latestMetric?.networkIn || 0) / 1024)} KB/s`}
          icon={Network}
          color="green"
        />
        <MetricCard
          title="Network Out"
          value={`${Math.round((latestMetric?.networkOut || 0) / 1024)} KB/s`}
          icon={Network}
          color="amber"
        />
      </div>

      {/* CPU & Memory Chart */}
      <RealtimeChart
        data={cpuMemData}
        title="CPU & Memory Usage"
        lines={cpuMemLines}
        yAxisUnit="%"
      />

      {/* Network Chart */}
      <RealtimeChart
        data={networkData}
        title="Network I/O (KB/s)"
        lines={networkLines}
        yAxisUnit=" KB"
      />

      {/* Server Alerts */}
      <AlertPanel alerts={alerts} title={`Alerts — ${server.hostname}`} />
    </div>
  );
}

export default ServerDetail;

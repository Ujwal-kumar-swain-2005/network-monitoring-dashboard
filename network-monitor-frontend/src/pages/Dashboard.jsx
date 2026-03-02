import React, { useState, useEffect, useCallback, useRef } from 'react';
import { Server, Cpu, MemoryStick, AlertTriangle, Activity } from 'lucide-react';
import MetricCard from '../components/MetricCard';
import RealtimeChart from '../components/RealtimeChart';
import ServerList from '../components/ServerList';
import AlertPanel from '../components/AlertPanel';
import { fetchServers, fetchAlerts } from '../services/api';
import { connectWebSocket, disconnectWebSocket } from '../services/websocket';

const MAX_CHART_POINTS = 30;

/**
 * Main dashboard page showing overview metrics, real-time charts,
 * server list, and alert panel.
 */
function Dashboard() {
  const [servers, setServers] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const latestMetrics = useRef({});

  // Load initial data from REST API
  useEffect(() => {
    async function loadData() {
      try {
        const [serversData, alertsData] = await Promise.all([
          fetchServers(),
          fetchAlerts(20),
        ]);
        setServers(serversData);
        setAlerts(alertsData);
      } catch (err) {
        console.error('Failed to load initial data:', err);
      } finally {
        setIsLoading(false);
      }
    }
    loadData();
  }, []);

  // Handle incoming real-time metric
  const handleMetric = useCallback((metric) => {
    // Update server list with latest metrics
    setServers((prev) => {
      const exists = prev.find((s) => s.id === metric.serverId);
      if (exists) {
        return prev.map((s) =>
          s.id === metric.serverId
            ? { ...s, cpuUsage: metric.cpuUsage, memoryUsage: metric.memoryUsage, status: 'ONLINE' }
            : s
        );
      } else {
        return [
          ...prev,
          {
            id: metric.serverId,
            hostname: metric.hostname,
            ipAddress: metric.ipAddress,
            status: 'ONLINE',
            cpuUsage: metric.cpuUsage,
            memoryUsage: metric.memoryUsage,
          },
        ];
      }
    });

    // Store latest metrics per server
    latestMetrics.current[metric.serverId] = metric;

    // Update chart data (aggregate across all servers)
    setChartData((prev) => {
      const allMetrics = Object.values(latestMetrics.current);
      const avgCpu = allMetrics.reduce((sum, m) => sum + (m.cpuUsage || 0), 0) / allMetrics.length;
      const avgMem = allMetrics.reduce((sum, m) => sum + (m.memoryUsage || 0), 0) / allMetrics.length;

      const newPoint = {
        time: new Date(metric.timestamp || Date.now()).toLocaleTimeString('en-US', {
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
          hour12: false,
        }),
        cpu: Math.round(avgCpu * 10) / 10,
        memory: Math.round(avgMem * 10) / 10,
      };

      const updated = [...prev, newPoint];
      return updated.length > MAX_CHART_POINTS ? updated.slice(-MAX_CHART_POINTS) : updated;
    });
  }, []);

  // Handle incoming real-time alert
  const handleAlert = useCallback((alert) => {
    setAlerts((prev) => [alert, ...prev].slice(0, 50));
  }, []);

  // Connect WebSocket
  useEffect(() => {
    connectWebSocket(handleMetric, handleAlert);
    return () => disconnectWebSocket();
  }, [handleMetric, handleAlert]);

  // Compute overview metrics
  const totalServers = servers.length;
  const onlineServers = servers.filter((s) => s.status === 'ONLINE').length;
  const avgCpu = servers.length > 0
    ? servers.reduce((sum, s) => sum + (s.cpuUsage || 0), 0) / servers.length
    : 0;
  const avgMemory = servers.length > 0
    ? servers.reduce((sum, s) => sum + (s.memoryUsage || 0), 0) / servers.length
    : 0;
  const activeAlerts = alerts.filter((a) => a.severity === 'CRITICAL' || a.severity === 'WARNING').length;

  // Chart configuration
  const chartLines = [
    { key: 'cpu', name: 'Avg CPU', color: '#22d3ee' },
    { key: 'memory', name: 'Avg Memory', color: '#a78bfa' },
  ];

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="flex flex-col items-center gap-3">
          <Activity className="w-8 h-8 text-brand-400 animate-spin" />
          <p className="text-surface-400 text-sm">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Overview Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <MetricCard
          title="Total Servers"
          value={totalServers}
          subtitle={`${onlineServers} online`}
          icon={Server}
          color="cyan"
        />
        <MetricCard
          title="Avg CPU Usage"
          value={`${avgCpu.toFixed(1)}%`}
          subtitle="Across all servers"
          icon={Cpu}
          color={avgCpu > 80 ? 'red' : avgCpu > 60 ? 'amber' : 'green'}
        />
        <MetricCard
          title="Avg Memory"
          value={`${avgMemory.toFixed(1)}%`}
          subtitle="Across all servers"
          icon={MemoryStick}
          color={avgMemory > 80 ? 'red' : avgMemory > 60 ? 'amber' : 'purple'}
        />
        <MetricCard
          title="Active Alerts"
          value={activeAlerts}
          subtitle={`${alerts.length} total`}
          icon={AlertTriangle}
          color={activeAlerts > 0 ? 'red' : 'green'}
        />
      </div>

      {/* Real-Time Chart */}
      <RealtimeChart
        data={chartData}
        title="System Performance Overview"
        lines={chartLines}
        yAxisUnit="%"
      />

      {/* Server List + Alert Panel */}
      <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
        <div className="xl:col-span-2">
          <ServerList servers={servers} />
        </div>
        <div>
          <AlertPanel alerts={alerts} />
        </div>
      </div>
    </div>
  );
}

export default Dashboard;

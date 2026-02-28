import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Server, Cpu, MemoryStick, ArrowRight } from 'lucide-react';

/**
 * Server status card displayed in the server list.
 * Shows hostname, IP, status, and current CPU/memory readings.
 */
function ServerStatus({ server }) {
  const navigate = useNavigate();
  const isOnline = server.status === 'ONLINE';

  const getCpuColor = (usage) => {
    if (!usage) return 'bg-surface-600';
    if (usage > 90) return 'bg-red-500';
    if (usage > 70) return 'bg-amber-500';
    return 'bg-emerald-500';
  };

  const getMemoryColor = (usage) => {
    if (!usage) return 'bg-surface-600';
    if (usage > 85) return 'bg-red-500';
    if (usage > 60) return 'bg-amber-500';
    return 'bg-cyan-500';
  };

  return (
    <div
      className="glass-card-hover p-5 cursor-pointer group"
      onClick={() => navigate(`/server/${server.id}`)}
    >
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-3">
          <div className={`p-2 rounded-lg ${isOnline ? 'bg-emerald-500/15' : 'bg-red-500/15'}`}>
            <Server className={`w-5 h-5 ${isOnline ? 'text-emerald-400' : 'text-red-400'}`} />
          </div>
          <div>
            <h3 className="text-sm font-semibold text-surface-100 group-hover:text-brand-400 transition-colors">
              {server.hostname}
            </h3>
            <p className="text-xs text-surface-500 font-mono">{server.ipAddress}</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <span className={isOnline ? 'status-online' : 'status-offline'}>
            <span className={`w-1.5 h-1.5 rounded-full mr-1.5 ${isOnline ? 'bg-emerald-400 animate-pulse-dot' : 'bg-red-400'}`} />
            {server.status}
          </span>
        </div>
      </div>

      {/* Metrics */}
      <div className="space-y-3">
        {/* CPU */}
        <div>
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-1.5">
              <Cpu className="w-3.5 h-3.5 text-surface-500" />
              <span className="text-xs text-surface-400">CPU</span>
            </div>
            <span className="text-xs font-semibold text-surface-300">
              {server.cpuUsage != null ? `${server.cpuUsage.toFixed(1)}%` : 'N/A'}
            </span>
          </div>
          <div className="metric-progress">
            <div
              className={`metric-progress-bar ${getCpuColor(server.cpuUsage)}`}
              style={{ width: `${server.cpuUsage || 0}%` }}
            />
          </div>
        </div>

        {/* Memory */}
        <div>
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-1.5">
              <MemoryStick className="w-3.5 h-3.5 text-surface-500" />
              <span className="text-xs text-surface-400">Memory</span>
            </div>
            <span className="text-xs font-semibold text-surface-300">
              {server.memoryUsage != null ? `${server.memoryUsage.toFixed(1)}%` : 'N/A'}
            </span>
          </div>
          <div className="metric-progress">
            <div
              className={`metric-progress-bar ${getMemoryColor(server.memoryUsage)}`}
              style={{ width: `${server.memoryUsage || 0}%` }}
            />
          </div>
        </div>
      </div>

      {/* View Details Link */}
      <div className="mt-4 flex items-center justify-end text-xs text-surface-500 group-hover:text-brand-400 transition-colors">
        View Details
        <ArrowRight className="w-3.5 h-3.5 ml-1 transform group-hover:translate-x-1 transition-transform" />
      </div>
    </div>
  );
}

export default ServerStatus;

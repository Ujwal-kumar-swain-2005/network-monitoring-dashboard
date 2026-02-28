import React from 'react';
import { AlertTriangle, AlertCircle, Info, Clock } from 'lucide-react';

/**
 * Alert panel displaying recent system alerts with severity badges.
 * 
 * @param {Array} alerts - Array of alert objects
 * @param {string} title - Panel title
 */
function AlertPanel({ alerts = [], title = 'Recent Alerts' }) {
  const severityConfig = {
    CRITICAL: {
      icon: AlertCircle,
      className: 'severity-critical',
      bg: 'bg-red-500/5 border-red-500/10',
      iconColor: 'text-red-400',
    },
    WARNING: {
      icon: AlertTriangle,
      className: 'severity-warning',
      bg: 'bg-amber-500/5 border-amber-500/10',
      iconColor: 'text-amber-400',
    },
    INFO: {
      icon: Info,
      className: 'severity-info',
      bg: 'bg-blue-500/5 border-blue-500/10',
      iconColor: 'text-blue-400',
    },
  };

  const formatTime = (timestamp) => {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  return (
    <div className="glass-card p-5 animate-fade-in">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-sm font-semibold text-surface-300">{title}</h3>
        {alerts.length > 0 && (
          <span className="text-xs text-surface-500">{alerts.length} alerts</span>
        )}
      </div>

      {alerts.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-8 text-surface-500">
          <AlertTriangle className="w-8 h-8 mb-2 opacity-30" />
          <p className="text-sm">No alerts</p>
          <p className="text-xs text-surface-600">All systems operating normally</p>
        </div>
      ) : (
        <div className="space-y-2 max-h-80 overflow-y-auto pr-1">
          {alerts.map((alert, index) => {
            const config = severityConfig[alert.severity] || severityConfig.INFO;
            const SeverityIcon = config.icon;

            return (
              <div
                key={alert.id || index}
                className={`flex items-start gap-3 p-3 rounded-xl border ${config.bg} animate-slide-up`}
                style={{ animationDelay: `${index * 50}ms` }}
              >
                <SeverityIcon className={`w-4 h-4 mt-0.5 flex-shrink-0 ${config.iconColor}`} />
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-0.5">
                    <span className={config.className}>{alert.severity}</span>
                    <span className="text-xs text-surface-500 font-mono">{alert.type}</span>
                  </div>
                  <p className="text-sm text-surface-300 leading-snug">{alert.message}</p>
                  <div className="flex items-center gap-3 mt-1.5">
                    {alert.serverHostname && (
                      <span className="text-xs text-surface-500">
                        Server: <span className="text-surface-400">{alert.serverHostname}</span>
                      </span>
                    )}
                    <div className="flex items-center gap-1 text-xs text-surface-600">
                      <Clock className="w-3 h-3" />
                      {formatTime(alert.createdAt)}
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default AlertPanel;

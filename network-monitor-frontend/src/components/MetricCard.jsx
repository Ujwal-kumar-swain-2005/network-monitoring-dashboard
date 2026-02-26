import React from 'react';

/**
 * Metric summary card with icon, value, label, and optional trend.
 * Used in the dashboard overview section.
 *
 * @param {string} title - Card label
 * @param {string|number} value - Primary metric value
 * @param {string} subtitle - Secondary description
 * @param {React.ReactNode} icon - Lucide icon component
 * @param {string} color - Color theme: 'cyan', 'green', 'amber', 'red', 'purple'
 * @param {string} trend - Optional trend direction: 'up', 'down', 'stable'
 * @param {string} trendValue - Trend percentage or value
 */
function MetricCard({ title, value, subtitle, icon: Icon, color = 'cyan', trend, trendValue }) {
  const colorMap = {
    cyan: {
      bg: 'bg-cyan-500/10',
      border: 'border-cyan-500/20',
      icon: 'text-cyan-400',
      glow: 'shadow-cyan-500/5',
    },
    green: {
      bg: 'bg-emerald-500/10',
      border: 'border-emerald-500/20',
      icon: 'text-emerald-400',
      glow: 'shadow-emerald-500/5',
    },
    amber: {
      bg: 'bg-amber-500/10',
      border: 'border-amber-500/20',
      icon: 'text-amber-400',
      glow: 'shadow-amber-500/5',
    },
    red: {
      bg: 'bg-red-500/10',
      border: 'border-red-500/20',
      icon: 'text-red-400',
      glow: 'shadow-red-500/5',
    },
    purple: {
      bg: 'bg-purple-500/10',
      border: 'border-purple-500/20',
      icon: 'text-purple-400',
      glow: 'shadow-purple-500/5',
    },
  };

  const colors = colorMap[color] || colorMap.cyan;

  const trendColors = {
    up: 'text-emerald-400',
    down: 'text-red-400',
    stable: 'text-surface-400',
  };

  const trendIcons = {
    up: '↑',
    down: '↓',
    stable: '→',
  };

  return (
    <div className={`glass-card p-5 ${colors.glow} shadow-lg animate-fade-in`}>
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-surface-400 mb-1">{title}</p>
          <p className="text-3xl font-bold text-surface-100 tracking-tight">{value}</p>
          {subtitle && (
            <p className="text-xs text-surface-500 mt-1">{subtitle}</p>
          )}
          {trend && trendValue && (
            <div className={`flex items-center gap-1 mt-2 text-sm ${trendColors[trend]}`}>
              <span>{trendIcons[trend]}</span>
              <span className="font-medium">{trendValue}</span>
            </div>
          )}
        </div>
        <div className={`p-3 rounded-xl ${colors.bg} border ${colors.border}`}>
          {Icon && <Icon className={`w-6 h-6 ${colors.icon}`} />}
        </div>
      </div>
    </div>
  );
}

export default MetricCard;

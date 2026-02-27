import React from 'react';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts';

/**
 * Custom tooltip for the Recharts chart.
 */
function CustomTooltip({ active, payload, label }) {
  if (active && payload && payload.length) {
    return (
      <div className="bg-surface-800/95 backdrop-blur-lg border border-surface-600/50 rounded-xl p-3 shadow-xl">
        <p className="text-xs text-surface-400 mb-2 font-mono">{label}</p>
        {payload.map((entry, index) => (
          <div key={index} className="flex items-center gap-2 text-sm">
            <div
              className="w-2 h-2 rounded-full"
              style={{ backgroundColor: entry.color }}
            />
            <span className="text-surface-300">{entry.name}:</span>
            <span className="font-semibold text-surface-100">
              {typeof entry.value === 'number' ? entry.value.toFixed(1) : entry.value}
              {entry.name.includes('CPU') || entry.name.includes('Memory') ? '%' : ''}
            </span>
          </div>
        ))}
      </div>
    );
  }
  return null;
}

/**
 * Real-time area chart component for displaying metric data over time.
 * Supports multiple data series with gradient fills.
 *
 * @param {Array} data - Array of data points with 'time' and metric fields
 * @param {string} title - Chart title
 * @param {Array} lines - Array of { key, name, color } for each data series
 * @param {string} yAxisUnit - Unit suffix for Y axis (e.g., '%', 'MB')
 */
function RealtimeChart({ data, title, lines = [], yAxisUnit = '%' }) {
  return (
    <div className="glass-card p-5 animate-fade-in">
      <h3 className="text-sm font-semibold text-surface-300 mb-4">{title}</h3>
      <div className="h-64">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data} margin={{ top: 5, right: 10, left: 0, bottom: 5 }}>
            <defs>
              {lines.map((line) => (
                <linearGradient
                  key={`gradient-${line.key}`}
                  id={`gradient-${line.key}`}
                  x1="0"
                  y1="0"
                  x2="0"
                  y2="1"
                >
                  <stop offset="5%" stopColor={line.color} stopOpacity={0.3} />
                  <stop offset="95%" stopColor={line.color} stopOpacity={0.02} />
                </linearGradient>
              ))}
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
            <XAxis
              dataKey="time"
              tick={{ fontSize: 11, fill: '#64748b' }}
              axisLine={{ stroke: '#334155' }}
              tickLine={false}
            />
            <YAxis
              tick={{ fontSize: 11, fill: '#64748b' }}
              axisLine={{ stroke: '#334155' }}
              tickLine={false}
              unit={yAxisUnit}
              domain={yAxisUnit === '%' ? [0, 100] : ['auto', 'auto']}
            />
            <Tooltip content={<CustomTooltip />} />
            <Legend
              wrapperStyle={{ fontSize: '12px', color: '#94a3b8' }}
              iconType="circle"
              iconSize={8}
            />
            {lines.map((line) => (
              <Area
                key={line.key}
                type="monotone"
                dataKey={line.key}
                name={line.name}
                stroke={line.color}
                strokeWidth={2}
                fill={`url(#gradient-${line.key})`}
                animationDuration={300}
                dot={false}
                activeDot={{ r: 4, strokeWidth: 2, stroke: line.color, fill: '#0f172a' }}
              />
            ))}
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

export default RealtimeChart;

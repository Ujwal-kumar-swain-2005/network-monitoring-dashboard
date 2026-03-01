import React from 'react';
import ServerStatus from './ServerStatus';
import { Server } from 'lucide-react';

/**
 * Displays a grid of server status cards.
 *
 * @param {Array} servers - Array of server objects
 * @param {string} title - Section title
 */
function ServerList({ servers = [], title = 'Monitored Servers' }) {
  return (
    <div className="animate-fade-in">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-sm font-semibold text-surface-300">{title}</h3>
        <span className="text-xs text-surface-500">
          {servers.filter((s) => s.status === 'ONLINE').length}/{servers.length} online
        </span>
      </div>

      {servers.length === 0 ? (
        <div className="glass-card flex flex-col items-center justify-center py-12 text-surface-500">
          <Server className="w-10 h-10 mb-3 opacity-30" />
          <p className="text-sm">No servers detected</p>
          <p className="text-xs text-surface-600 mt-1">Waiting for agent connections...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {servers.map((server) => (
            <ServerStatus key={server.id} server={server} />
          ))}
        </div>
      )}
    </div>
  );
}

export default ServerList;

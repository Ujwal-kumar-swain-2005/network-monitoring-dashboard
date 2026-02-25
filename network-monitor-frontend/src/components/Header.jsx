import React, { useState, useEffect } from 'react';
import { Activity, Wifi, WifiOff } from 'lucide-react';

/**
 * Top header bar showing connection status and current time.
 */
function Header() {
  const [currentTime, setCurrentTime] = useState(new Date());
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Check WebSocket connection status periodically
  useEffect(() => {
    const checkConnection = setInterval(() => {
      // Check if the WebSocket client module reports as connected
      import('../services/websocket.js').then((ws) => {
        setIsConnected(ws.isConnected());
      });
    }, 2000);
    return () => clearInterval(checkConnection);
  }, []);

  return (
    <header className="flex items-center justify-between px-6 py-4 bg-surface-900/60 backdrop-blur-xl border-b border-surface-700/50">
      {/* Left: Page Title */}
      <div className="flex items-center gap-3">
        <Activity className="w-5 h-5 text-brand-400 animate-pulse-slow" />
        <h2 className="text-lg font-semibold text-surface-100">Real-Time Monitoring</h2>
      </div>

      {/* Right: Status + Time */}
      <div className="flex items-center gap-6">
        {/* Connection Status */}
        <div className="flex items-center gap-2">
          {isConnected ? (
            <>
              <Wifi className="w-4 h-4 text-emerald-400" />
              <span className="text-sm text-emerald-400 font-medium">Live</span>
              <span className="relative flex h-2 w-2">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
              </span>
            </>
          ) : (
            <>
              <WifiOff className="w-4 h-4 text-surface-500" />
              <span className="text-sm text-surface-500 font-medium">Disconnected</span>
            </>
          )}
        </div>

        {/* Current Time */}
        <div className="px-3 py-1.5 bg-surface-800/80 rounded-lg border border-surface-700/50">
          <span className="text-sm font-mono text-surface-300">
            {currentTime.toLocaleTimeString('en-US', {
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: false,
            })}
          </span>
        </div>
      </div>
    </header>
  );
}

export default Header;

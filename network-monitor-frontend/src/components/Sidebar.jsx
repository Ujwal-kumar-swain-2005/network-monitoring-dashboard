import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Activity, 
  Server, 
  Bell, 
  Settings,
  Radio
} from 'lucide-react';

/**
 * Sidebar navigation component with brand logo and nav links.
 */
function Sidebar() {
  const navItems = [
    { to: '/', icon: LayoutDashboard, label: 'Dashboard' },
  ];

  return (
    <aside className="hidden lg:flex flex-col w-64 bg-surface-900/80 backdrop-blur-xl border-r border-surface-700/50">
      {/* Brand */}
      <div className="flex items-center gap-3 px-6 py-5 border-b border-surface-700/50">
        <div className="p-2 bg-brand-500/20 rounded-xl">
          <Radio className="w-6 h-6 text-brand-400" />
        </div>
        <div>
          <h1 className="text-lg font-bold gradient-text">NetPulse</h1>
          <p className="text-xs text-surface-400">Network Monitor</p>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 px-4 py-6 space-y-1">
        <p className="px-3 mb-3 text-xs font-semibold text-surface-500 uppercase tracking-wider">
          Main Menu
        </p>
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all duration-200 ${
                isActive
                  ? 'bg-brand-500/15 text-brand-400 border border-brand-500/20'
                  : 'text-surface-400 hover:text-surface-200 hover:bg-surface-800/80'
              }`
            }
          >
            <item.icon className="w-5 h-5" />
            {item.label}
          </NavLink>
        ))}
      </nav>

      {/* Footer */}
      <div className="px-4 py-4 border-t border-surface-700/50">
        <div className="flex items-center gap-3 px-3 py-2">
          <div className="w-8 h-8 rounded-full bg-gradient-to-br from-brand-400 to-cyan-600 flex items-center justify-center text-sm font-bold text-white">
            N
          </div>
          <div>
            <p className="text-sm font-medium text-surface-200">NetPulse</p>
            <p className="text-xs text-surface-500">v1.0.0</p>
          </div>
        </div>
      </div>
    </aside>
  );
}

export default Sidebar;

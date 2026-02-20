import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import ServerDetail from './pages/ServerDetail';

/**
 * Root application component with sidebar layout and routing.
 */
function App() {
  return (
    <div className="flex h-screen overflow-hidden bg-surface-950">
      {/* Sidebar Navigation */}
      <Sidebar />

      {/* Main Content Area */}
      <div className="flex flex-col flex-1 overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-6">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/server/:id" element={<ServerDetail />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}

export default App;

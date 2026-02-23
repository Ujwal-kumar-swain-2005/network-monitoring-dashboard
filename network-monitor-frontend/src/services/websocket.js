import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

/**
 * WebSocket service for real-time metric and alert updates.
 * Connects to the backend STOMP broker via SockJS.
 */

const WS_URL = import.meta.env.VITE_WS_URL || '/ws';

let stompClient = null;
let metricSubscription = null;
let alertSubscription = null;

/**
 * Connects to the WebSocket server and subscribes to metric and alert topics.
 * 
 * @param {Function} onMetric - Callback when a metric update is received
 * @param {Function} onAlert - Callback when an alert is received
 * @param {Function} onConnect - Callback when connection is established
 * @param {Function} onDisconnect - Callback when connection is lost
 */
export function connectWebSocket(onMetric, onAlert, onConnect, onDisconnect) {
  stompClient = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    debug: (str) => {
      // Uncomment for debugging: console.log('[STOMP]', str);
    },
    onConnect: () => {
      console.log('[WebSocket] Connected to server');

      // Subscribe to real-time metrics
      metricSubscription = stompClient.subscribe('/topic/metrics', (message) => {
        try {
          const metric = JSON.parse(message.body);
          if (onMetric) onMetric(metric);
        } catch (e) {
          console.error('[WebSocket] Failed to parse metric:', e);
        }
      });

      // Subscribe to alerts
      alertSubscription = stompClient.subscribe('/topic/alerts', (message) => {
        try {
          const alert = JSON.parse(message.body);
          if (onAlert) onAlert(alert);
        } catch (e) {
          console.error('[WebSocket] Failed to parse alert:', e);
        }
      });

      if (onConnect) onConnect();
    },
    onDisconnect: () => {
      console.log('[WebSocket] Disconnected');
      if (onDisconnect) onDisconnect();
    },
    onStompError: (frame) => {
      console.error('[WebSocket] STOMP error:', frame.headers['message']);
      console.error('[WebSocket] Details:', frame.body);
    },
  });

  stompClient.activate();
}

/**
 * Disconnects from the WebSocket server.
 */
export function disconnectWebSocket() {
  if (metricSubscription) {
    metricSubscription.unsubscribe();
    metricSubscription = null;
  }
  if (alertSubscription) {
    alertSubscription.unsubscribe();
    alertSubscription = null;
  }
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
}

/**
 * Returns whether the WebSocket is currently connected.
 */
export function isConnected() {
  return stompClient !== null && stompClient.connected;
}

package com.networkmonitor.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * WebSocket client that connects to the backend's STOMP endpoint
 * and sends metric data from the monitoring agent.
 */
@Service
@Slf4j
public class AgentWebSocketClient {
    private static final Logger log =  LoggerFactory.getLogger(AgentWebSocketClient.class);
    private StompSession session;
    private WebSocketStompClient stompClient;

    @Value("${monitor.backend.ws-url}")
    private String wsUrl;

    @PostConstruct
    public void init() {
        // Configure SockJS transport
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(sockJsClient);

        // Configure Jackson message converter with Java Time support
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        stompClient.setMessageConverter(converter);

        connect();
    }

    /**
     * Establishes connection to the backend WebSocket endpoint.
     * Retries up to 10 times with 5-second intervals on failure.
     */
    private void connect() {
        int maxRetries = 10;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("Connecting to backend WebSocket: {} (attempt {}/{})", wsUrl, attempt, maxRetries);
                session = stompClient.connectAsync(wsUrl, new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        log.info("Successfully connected to backend WebSocket");
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command,
                                                StompHeaders headers, byte[] payload, Throwable exception) {
                        log.error("WebSocket error: {}", exception.getMessage());
                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        log.error("Transport error: {}", exception.getMessage());
                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }
                }).get(10, TimeUnit.SECONDS);

                log.info("WebSocket connection established successfully");
                return;

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.warn("Connection attempt {} failed: {}", attempt, e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
        log.error("Failed to connect to backend after {} attempts", maxRetries);
    }

    /**
     * Sends a metric payload to the backend via STOMP.
     * Attempts reconnection if the session is disconnected.
     */
    public void sendMetric(MetricPayload payload) {
        if (session == null || !session.isConnected()) {
            log.warn("WebSocket session not connected, attempting reconnect...");
            connect();
        }

        if (session != null && session.isConnected()) {
            session.send("/app/metrics", payload);
            log.debug("Sent metric to backend: CPU={}%, MEM={}%",
                    payload.getCpuUsage(), payload.getMemoryUsage());
        } else {
            log.error("Failed to send metric — WebSocket not connected");
        }
    }

    /**
     * Returns whether the WebSocket session is currently connected.
     */
    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    @PreDestroy
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("Disconnected from backend WebSocket");
        }
    }
}

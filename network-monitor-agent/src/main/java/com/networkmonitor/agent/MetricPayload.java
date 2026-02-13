package com.networkmonitor.agent;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Metric data payload sent from agent to backend via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricPayload {

    private String hostname;
    private String ipAddress;
    private Double cpuUsage;
    private Double memoryUsage;
    private Long networkIn;
    private Long networkOut;
    private LocalDateTime timestamp;
}

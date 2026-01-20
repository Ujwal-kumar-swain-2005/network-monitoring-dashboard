package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for metric data.
 * Used for both incoming agent data and outgoing WebSocket broadcasts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricDTO {

    private Long serverId;
    private String hostname;
    private String ipAddress;
    private Double cpuUsage;
    private Double memoryUsage;
    private Long networkIn;
    private Long networkOut;
    private LocalDateTime timestamp;
}

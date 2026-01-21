package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for server information.
 * Includes latest CPU/memory readings for dashboard display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerDTO {

    private Long id;
    private String hostname;
    private String ipAddress;
    private String status;
    private LocalDateTime lastSeen;
    private Double cpuUsage;
    private Double memoryUsage;
}

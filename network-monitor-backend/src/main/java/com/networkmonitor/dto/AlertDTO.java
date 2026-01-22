package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for alert information.
 * Enriched with server hostname for frontend display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDTO {

    private Long id;
    private Long serverId;
    private String serverHostname;
    private String type;
    private String message;
    private String severity;
    private LocalDateTime createdAt;
}

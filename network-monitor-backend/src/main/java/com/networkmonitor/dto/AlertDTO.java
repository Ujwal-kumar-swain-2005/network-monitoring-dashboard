package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for alert information.
 * Enriched with server hostname for frontend display.
 */

public class AlertDTO {

    private Long id;
    private Long serverId;
    private String serverHostname;
    private String type;
    private String message;
    private String severity;
    private LocalDateTime createdAt;

    public AlertDTO() {
    }

    public AlertDTO(Long id, Long serverId, String serverHostname, String type, String message, String severity, LocalDateTime createdAt) {
        this.id = id;
        this.serverId = serverId;
        this.serverHostname = serverHostname;
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

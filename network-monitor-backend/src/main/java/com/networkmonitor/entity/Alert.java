package com.networkmonitor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a system alert triggered when metric thresholds are exceeded.
 * Types include CPU_HIGH, MEMORY_HIGH, SERVER_DOWN, etc.
 * Severity levels: CRITICAL, WARNING, INFO.
 */
@Entity
@Table(name = "alerts", indexes = {
    @Index(name = "idx_alerts_server_id", columnList = "server_id"),
    @Index(name = "idx_alerts_created_at", columnList = "created_at")
})

public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    @Builder.Default
    private String severity = "WARNING";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Alert() {
    }

    public Alert(Long id, Long serverId, String type, String message, String severity, LocalDateTime createdAt) {
        this.id = id;
        this.serverId = serverId;
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.createdAt = createdAt;
    }

    public Alert(Long id, String type, String message, String severity, LocalDateTime now) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.createdAt = now;
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

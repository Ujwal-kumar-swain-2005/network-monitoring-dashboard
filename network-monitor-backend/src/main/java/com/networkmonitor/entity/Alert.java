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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

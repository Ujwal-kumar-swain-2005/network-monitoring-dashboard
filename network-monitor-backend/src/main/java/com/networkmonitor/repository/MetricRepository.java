package com.networkmonitor.repository;

import com.networkmonitor.entity.Metric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Metric entity operations.
 * Provides methods for querying metrics by server, time range, and latest values.
 */
@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

    List<Metric> findByServerIdOrderByTimestampDesc(Long serverId, Pageable pageable);

    List<Metric> findByServerIdAndTimestampBetweenOrderByTimestampAsc(
            Long serverId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM Metric m WHERE m.serverId = :serverId ORDER BY m.timestamp DESC LIMIT 1")
    Optional<Metric> findLatestByServerId(@Param("serverId") Long serverId);

    @Query("SELECT m FROM Metric m WHERE m.serverId = :serverId ORDER BY m.timestamp DESC")
    List<Metric> findRecentByServerId(@Param("serverId") Long serverId, Pageable pageable);

    long countByServerId(Long serverId);
}

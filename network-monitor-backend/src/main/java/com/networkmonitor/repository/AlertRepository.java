package com.networkmonitor.repository;

import com.networkmonitor.entity.Alert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Alert entity operations.
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByServerIdOrderByCreatedAtDesc(Long serverId);

    List<Alert> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Alert> findBySeverityOrderByCreatedAtDesc(String severity);

    long countByServerId(Long serverId);
}

package com.networkmonitor.repository;

import com.networkmonitor.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Server entity operations.
 */
@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    Optional<Server> findByHostname(String hostname);

    List<Server> findByStatus(String status);

    List<Server> findByLastSeenBefore(LocalDateTime threshold);
}

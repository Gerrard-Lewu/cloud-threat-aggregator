package com.threataggregator.api;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ThreatRepository extends JpaRepository<ThreatIndicator, Long> {
    Optional<ThreatIndicator> findByIpAddress(String ipAddress);
}
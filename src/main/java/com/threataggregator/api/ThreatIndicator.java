package com.threataggregator.api;

import jakarta.persistence.*;

@Entity
public class ThreatIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String threatLevel;
    private String source;

    // Empty constructor required by JPA
    public ThreatIndicator() {}

    public ThreatIndicator(String ipAddress, String threatLevel, String source) {
        this.ipAddress = ipAddress;
        this.threatLevel = threatLevel;
        this.source = source;
    }

    // Getters
    public String getIpAddress() { return ipAddress; }
    public String getThreatLevel() { return threatLevel; }
    public String getSource() { return source; }
}
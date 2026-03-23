package com.threataggregator.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignores any extra data AbuseIPDB sends
@JsonIgnoreProperties(ignoreUnknown = true)
public record AbuseIPData(String ipAddress, int abuseConfidenceScore) {
}

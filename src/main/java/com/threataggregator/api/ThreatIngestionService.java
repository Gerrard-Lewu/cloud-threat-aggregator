package com.threataggregator.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ThreatIngestionService {

    private final ThreatRepository repository;
    private final RestClient restClient;

    // Grabs the secret key from application.properties file
    @Value("${abuseipdb.api.key}")
    private String apiKey;

    public ThreatIngestionService(ThreatRepository repository) {
        this.repository = repository;
        this.restClient = RestClient.create();
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchRealThreatData() {
        System.out.println("Starting real threat data ingestion from AbuseIPDB...");

        try {
            // Makes the request to AbuseIPDB
            AbuseIPResponse response = restClient.get()
                    .uri("https://api.abuseipdb.com/api/v2/blacklist?confidenceMinimum=90")
                    .header("Key", apiKey)
                    .header("Accept", "application/json")
                    .retrieve()
                    .body(AbuseIPResponse.class);

            // Grabs available data and saves it to database
            if (response != null && response.data() != null) {
                List<AbuseIPData> badIps = response.data();
                System.out.println("Successfully downloaded " + badIps.size() + " malicious IPs.");

                for (AbuseIPData data : badIps) {
                    if (repository.findFirstByIpAddressOrderByIdDesc(data.ipAddress()).isEmpty()) {
                        ThreatIndicator threat = new ThreatIndicator(
                                data.ipAddress(),
                                "HIGH (Score: " + data.abuseConfidenceScore() + ")",
                                "AbuseIPDB"
                        );
                        repository.save(threat);
                    }
                }
                System.out.println("Database update complete!");
            }

        } catch (Exception e) {
            System.err.println("Failed to fetch threat data: " + e.getMessage());
        }
    }
}

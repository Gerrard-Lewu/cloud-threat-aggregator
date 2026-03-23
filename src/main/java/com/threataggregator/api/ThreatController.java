package com.threataggregator.api;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/threats")
public class ThreatController {

    private final ThreatRepository repository;

    public ThreatController(ThreatRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/check")
    public Object checkIp(@RequestParam String ip) {
        Optional<ThreatIndicator> indicator = repository.findFirstByIpAddressOrderByIdDesc(ip);

        if (indicator.isPresent()) {
            return indicator.get(); // Returns the malicious IP details as JSON
        } else {
            return "{\"status\": \"SAFE\", \"message\": \"IP not found in threat database\"}";
        }
    }

    // Temporary endpoint to manually add fake threat data to test
    @PostMapping("/add-test-data")
    public String addTestData() {
        repository.save(new ThreatIndicator("192.168.1.99", "HIGH", "ManualTest"));
        return "Test data added!";
    }
}
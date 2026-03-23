package com.threataggregator.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Turns on automated worker
public class AggregatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApiApplication.class, args);
	}

}

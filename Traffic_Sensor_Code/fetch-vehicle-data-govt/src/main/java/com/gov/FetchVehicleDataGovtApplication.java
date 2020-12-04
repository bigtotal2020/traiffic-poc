package com.gov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FetchVehicleDataGovtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchVehicleDataGovtApplication.class, args);
	}

}

package com.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotifyMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifyMessageApplication.class, args);
	}

}

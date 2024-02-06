package org.smartblackbox.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka server for Lease application.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class LeaseEurekaServer {

	public static void main(String[] args) {
		SpringApplication.run(LeaseEurekaServer.class, args);
	}

}

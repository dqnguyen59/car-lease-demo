package org.smartblackbox.carleasedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Lease Demo application.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
//@OpenAPIDefinition(servers = {@Server(url = "/Lease", description = "Default Server URL")})
@SpringBootApplication
@EnableDiscoveryClient
public class CarApplication {

  /**
   * A constructor to create a CarApplication.
   * 
   */
  public CarApplication() {
    
  }
  
  /**
   * The application starts here.
   * 
   * @param args arguments of the application
   */
  public static void main(String[] args) {
    SpringApplication.run(CarApplication.class, args);
  }

}

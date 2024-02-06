package org.smartblackbox.carleasedemo.controller;

import java.util.List;
import org.smartblackbox.carleasedemo.datamodel.dto.response.SingleMessageDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest Controller for Greet end points.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@RequestMapping("/api/v1/user")
@RestController
public class GreetController {

  @Autowired
  Environment environment;

  @Autowired
  @Lazy
  private EurekaClient eurekaClient;

  @Autowired
  private DiscoveryClient discoveryClient;

  @Value("${spring.application.name}")
  private String appName;

  /**
   * This is a constructor to create a GreetController object.
   * 
   */
  public GreetController() {
    
  }
  
  /**
   * Gets the welcome greet message.
   * 
   * @return a the greet message
   */
  @Operation(summary = "Greet")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns as simple greeting message", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @GetMapping("/greet")
  public String getGreetings() {
    String port = environment.getProperty("server.port");
    log.info("Welcome! from port number " + port);

    System.out.println("Fetching greetings type for locale appname = " + appName);


    Application app = eurekaClient.getApplication(appName);

    String s = "";
    if (app != null) {
      s = String.format("Welcome from '%s'! ", app.getName());
    }
    else {
      System.out.println("Error: app = null");
      s = "Error: Unknown appname!";
    }
    return s + "; Port: " + port;
  }

  /**
   * Gets a list of instances from the current service name.
   * 
   * @param serviceName override the default service name  
   * @return a list of instances
   */
  @Operation(summary = "Show all instances")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns all instances", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @GetMapping("/instances")
  public List<ServiceInstance> getInstances(
      @RequestParam(name = "serviceName", required = false) String serviceName) {
    System.out.println("Fetching instances for locale appname = " + serviceName);

    if (serviceName == null || serviceName.isEmpty()) serviceName = appName;

    List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

    if (instances != null) {
      return instances;
    }
    else {
      System.out.println("Error: instances = null");
      return null;
    }

  }

}


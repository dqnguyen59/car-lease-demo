package org.smartblackbox.carleasedemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration of the authentication Client.
 * 
 * The configuration is stored in file application.properties.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org> and contributor
 *
 */
@Configuration
public class AuthenticationClientConfig {
  
  /**
   * The protocol: [http | https]
   */
  @Value("${carlease.gateway.protocol}")
  public String protocol;
  
  /**
   * The host or domain name of the authentication server or service. 
   */
  @Value("${carlease.gateway.host}")
  public String host;
  
  /**
   * The port
   */
  @Value("${carlease.gateway.port}")
  public String port;
  
  /**
   * The endpoint
   */
  @Value("${carlease.gateway.endpoint}")
  public String endpoint;
  
  /**
   * A constructor to create a AuthenticationConfig.
   * 
   */
  public AuthenticationClientConfig() {
    
  }
  
}

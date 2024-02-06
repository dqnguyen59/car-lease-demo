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
public class ServerConfig {

  /**
   * The root username
   */
  @Value("${carlease.root.username}")
  public String rootUsername;

  /**
   * The root password
   */
  @Value("${carlease.root.password}")
  public String rootPassword;

  /**
   * A constructor to create a ServerConfig.
   * 
   */
  public ServerConfig() {

  }

}

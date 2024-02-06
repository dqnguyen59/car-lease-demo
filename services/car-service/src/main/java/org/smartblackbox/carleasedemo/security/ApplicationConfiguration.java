package org.smartblackbox.carleasedemo.security;

import java.util.Optional;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Required configurations for the application.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Configuration
public class ApplicationConfiguration {

  @Autowired
  AuthenticationClient authClient;

  /**
   * A constructor to create a ApplicationConfiguration.
   * 
   */
  public ApplicationConfiguration() {
    
  }
  
  /**
   * Gets the user detail information.
   * 
   * @return the user detail information
   */
  @Bean
  UserDetailsService userDetailsService() {
    return username -> Optional.of(authClient.getSignedInUser())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**
   * Gets the BCrypt password encoder
   * 
   * @return the BCrypt password encoder
   */
  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Gets the authentication manager.
   * 
   * @param config the config
   * @return the authentication manager
   * @throws Exception throws the exception if failed
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Gets the authentication provider.
   * 
   * @return the authentication providwer
   */
  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

}

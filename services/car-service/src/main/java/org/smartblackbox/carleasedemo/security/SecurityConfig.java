package org.smartblackbox.carleasedemo.security;

import java.util.List;
import org.smartblackbox.carleasedemo.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration of the security. 
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  /**
   * Whitelisting the following endpoints.
   */
  private static final String[] AUTH_WHITELIST = {
      "/api/v1/**",
      "/h2-console/**",
      "/swagger-resources/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v2/api-docs",
      "/webjars/**"
  };

  private AuthenticationProvider authenticationProvider;
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * A constructor to create a SecurityConfig.
   * 
   * @param jwtAuthenticationFilter the authentication filter
   * @param authenticationProvider the authentication provider
   */
  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Make sure the endpoints from AUTH_WHITELIST is permitted.
   * 
   * @param httpSecurity the http security object
   * @return SecurityFilterChain object
   * @throws Exception throws an exception if failed
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity
        .headers(header -> header.frameOptions(frame -> frame.disable()))
        .csrf(csrf -> csrf.disable())
        .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated()
            )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  /**
   * Allows the origins of the given domains.
   * 
   * @return CorsConfigurationSource object
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of(
        "http://localhost:8080",
        "http://localhost:8888",
        "https://tomcat.smartblackbox.org"
        ));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**",configuration);

    return source;
  }    

}
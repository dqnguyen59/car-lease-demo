package org.smartblackbox.carleasedemo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Added for SwaggerUI Token Authentication.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Smart Black Box Lease Demo API - Customer-Service", version = "v1"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
    )
public class OpenApi30Config {

  /**
   * This is a constructor to create a OpenApi30Config object.
   * 
   */
  public OpenApi30Config() {
    
  }
  
  /**
   * Performs object mapping.
   * 
   * @return the ModelMapper object
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

}

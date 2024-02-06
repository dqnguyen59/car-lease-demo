package org.smartblackbox.carleasedemo.service;

import java.util.Arrays;
import org.smartblackbox.carleasedemo.config.AuthenticationClientConfig;
import org.smartblackbox.carleasedemo.datamodel.dto.response.TokenResponseDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * The authentication Client.
 * 
 * Validates the token from the authentication header via the user-service using RestClient.
 * 
 * The user-service will provides relevant information about the signed in user based on the token.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Service
public class AuthenticationClient {

  /**
   * The configuration of the Authentication.
   */
  @Autowired
  private AuthenticationClientConfig authenticationClientConfig;

  /**
   * The signed in user.
   */
  private User signedInUser;

  /**
   * A constructor to create a AuthenticationClient.
   * 
   */
  public AuthenticationClient() {
    
  }
  
  /**
   * Gets the saved signed in user.
   * 
   * @return the saved signed in user
   */
  public User getSignedInUser() {
    if (signedInUser == null) {
      signedInUser = User.builder()
          .roles(Arrays.asList())
          .build();
    }
    return signedInUser;
  }

  /**
   * Validate token from the header.
   * The token is validated via user-service using RestClient and the retrieved data is stored in TokenResponseDTO.
   * 
   * @return the TokenResponseDTO object
   */
  public TokenResponseDTO validateToken() {
    log.info("validateToken()");
    try { 
      String authHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
      //log.info("Header: " + authHeader);

      String token = "!!!_dummy_invalid_token_!!!";

      if (authHeader != null) {
        token = authHeader.substring(7);
      }

      String url = String.format(
          "%s://%s:%s",
          authenticationClientConfig.protocol,
          authenticationClientConfig.host,
          authenticationClientConfig.port
          ); 

      log.info("url: " + url + authenticationClientConfig.endpoint);
      log.info("token: " + token);

      RestClient restClient = RestClient.builder()
          .baseUrl(url)
          .build();

      TokenResponseDTO response = restClient.get()
          .uri(authenticationClientConfig.endpoint, token)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .body(TokenResponseDTO.class);


      log.info("response: " + response);
      return response;
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    return new TokenResponseDTO();
  }

}

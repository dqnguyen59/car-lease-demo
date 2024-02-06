package org.smartblackbox.carleasedemo.security.jwt;

import java.io.IOException;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.dto.response.TokenResponseDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The JSON Web Token authentication filter intercepts the API requests 
 * prior to the REST controller end points.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * The authentication client info. 
   */
  @Autowired
  AuthenticationClient authClient;

  /**
   * The JSON Web Token service.
   */
  @Autowired
  private JwtService jwtService;

  /**
   * A constructor to create a role.
   */
  public JwtAuthenticationFilter() {
    
  }
  
  /**
   * Processing of the API requests prior to the REST controller end points.
   * This will use the token from the header and validate the token via user-service.
   * The validation info will be stored in the variable user of AuthenticationClient.getDefaultUser.
   * 
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
      ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    String token = null;

    if (authHeader != null) {
      token = authHeader.substring(7);
    }

    String username = null;
    
    if (token != null) {
      try {
        username = jwtService.extractUsername(token);
      } catch (Exception exception) {
        log.error("Error: " + exception.getMessage());
      }
    }

    Optional<User> user = Optional.of(authClient.getSignedInUser());

    TokenResponseDTO tokenDTO = authClient.validateToken();
    
    boolean isTokenValid = token != null && tokenDTO.isTokenValid();

    if (isTokenValid) {
      user.get().setRoles(tokenDTO.getRoles());
    }
    
    // If token is valid, then set the authorization level by the roles
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        user.get(),
        null,
        isTokenValid? user.get().getAuthorities() : null
        );

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);

    user.get().setId(tokenDTO.getUserId());
    user.get().setUsername(username);
    user.get().setTokenValid(isTokenValid);

//    log.info("doFilterInternal(): token = " + token);
//    log.info("doFilterInternal(): message: " + tokenDTO.getMessage());
//    log.info("doFilterInternal(): user id: " + user.get().getId());
//    log.info("doFilterInternal(): username = " + username);

    filterChain.doFilter(request, response);
  }
}

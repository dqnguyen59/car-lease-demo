package org.smartblackbox.carleasedemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.datamodel.response.TokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.google.gson.Gson;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
//@Component
public class SpringUtils {

  @Value("${server.port}")
  private static String port;

  public static Authentication getAuthentication() {
    return  SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Checks if the user is authenticated and logged in.
   * If user is not authenticated or logged in,
   * then it always throws AccessDeniedException().
   * 
   * Any HTTP request must be provided with a token.
   * A token is provided when user a signed in.
   * 
   * @return the user if logged in, otherwise throws AccessDeniedException().
   */
  public static User getAndValidateUser() {
    try {
      User user = (User) getAuthentication().getPrincipal();
      if (user != null && user.isLoggedIn() && user.isTokenValid() && user.isAccountNonLocked()) {
        return user;
      }
      throw new AccessDeniedException("");
    }
    catch (Exception e) {
      throw new AccessDeniedException("");
    }
  }  	

  /**
   * Validate token from the header.
   * If token is invalid, then it will throw an AccessDeniedException.
   * 
   * @return
   */
  public static TokenResponseDTO validateToken() {
    String authHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    System.out.println("Header: " + authHeader);

    String token = "!!! dummy invalid token !!!";

    if (authHeader != null) {
      token = authHeader.substring(7);
    }

    // TODO: make host, port, username and password configurable.
    String host = "localhost";
    String port = "8080";
    String username = "root";
    String password = "secret";
    String endpoint = "/api/v1/auth/validateToken/";
    String url = "http://" + username + ":" + password + "@" + host + ":" + port + endpoint + token;
    System.out.println("url: " + url);
    String command = "curl -X GET " + url;

    System.out.println("command: '" + command + "'");
    boolean isTokenValid = false;

    try {
      ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
      processBuilder.directory(new File("/tmp/"));
      Process process = processBuilder.start();
      InputStream inputStream = process.getInputStream();

      String json = new BufferedReader(
          new InputStreamReader(inputStream, StandardCharsets.UTF_8))
          .lines()
          .collect(Collectors.joining("\n"));

      System.out.println("Response: '" + json + "'");

      TokenResponseDTO tokenResponseDTO = new Gson().fromJson(json, TokenResponseDTO.class);

      if (tokenResponseDTO != null) {
        System.out.println("tokenResponseDTO: " + tokenResponseDTO.getMessage());
        System.out.println("tokenResponseDTO: " + tokenResponseDTO.isTokenValid());
        if (tokenResponseDTO.getRoles() != null) {
          System.out.println("tokenResponseDTO: " + tokenResponseDTO.getRoles().
              stream().
              map(Role::getTypeAsString).
              collect(Collectors.joining(",")));
        }
        return tokenResponseDTO;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    throw new AccessDeniedException("");
  }

  public static <S, T> List<T> mapList(ModelMapper modelMapper, List<S> source, Class<T> targetClass) {
    return source
        .stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }
}

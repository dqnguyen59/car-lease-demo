package org.smartblackbox.carleasedemo.controller;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.smartblackbox.carleasedemo.datamodel.dto.SignInDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.datamodel.response.SingleMessageDTO;
import org.smartblackbox.carleasedemo.datamodel.response.TokenResponseDTO;
import org.smartblackbox.carleasedemo.datamodel.response.UserDTO;
import org.smartblackbox.carleasedemo.datamodel.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.security.jwt.JwtService;
import org.smartblackbox.carleasedemo.service.AuthenticationService;
import org.smartblackbox.carleasedemo.service.UserService;
import org.smartblackbox.carleasedemo.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@RequestMapping("/api/v1/auth")
@RestController
@SecurityRequirement(name = "bearerAuth")
public class AuthenticationController {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationService authenticationService;

  @Operation(summary = "Sign in user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User signed in succesfully",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "401", description = "Bad credentials", 
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PostMapping("/signin")
  public ResponseEntity<UserDTO> signIn(@RequestBody SignInDTO loginUserDTO) {
    User user = authenticationService.authenticate(loginUserDTO);
    User userResponse = authenticationService.signin(user);
    UserDTO userDTO = modelMapper.map(userResponse, UserDTO.class);

    return ResponseEntity.ok(userDTO);
  }

  @Operation(summary = "Sign out user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Sign out user",  
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "401", description = "Bad credentials", 
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PostMapping("/signout")
  public ResponseEntity<SingleMessageDTO> signOut() {
    try {
      User user = (User) SpringUtils.getAuthentication().getPrincipal();
      String response = authenticationService.signout(user);

      SingleMessageDTO msgDTO = new SingleMessageDTO();
      msgDTO.setMessage(response);

      return ResponseEntity.ok(msgDTO);
    }
    catch (Exception e) {
      throw new AccessDeniedException("");
    }
  }

  @Operation(summary = "validateToken")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Determine if the given token is valid."
          + "If the token is valid, then user roles are returned.",  
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @GetMapping("/validateToken/{token}")
  public ResponseEntity<TokenResponseDTO> validateToken(@PathVariable("token") String token) {
    String username = null;
    try {
      username = jwtService.extractUsername(token);
    } catch (Exception exception) {
      //log.warn("Warning: " + exception.getMessage());
    }

    boolean isTokenValid = false;
    String msg = "Invalid token!";
    TokenResponseDTO tokenDTO = new TokenResponseDTO();

    if (username != null) {
      Optional<User> user = userService.findByUsername(username);

      isTokenValid = jwtService.isTokenValid(token, user.get());

      user.get().setTokenValid(isTokenValid);
      userService.save(user.get());

      msg = "Token accepted";
      tokenDTO.setUserId(user.get().getId());
      tokenDTO.setRoles(user.get().getRoles());
    }

    tokenDTO.setMessage(msg);
    tokenDTO.setTokenValid(isTokenValid);

    return ResponseEntity.ok(tokenDTO);
  }

}

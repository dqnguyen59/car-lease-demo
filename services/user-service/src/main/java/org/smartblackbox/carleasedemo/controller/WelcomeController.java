package org.smartblackbox.carleasedemo.controller;

import org.smartblackbox.carleasedemo.datamodel.dto.response.SingleMessageDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.smartblackbox.carleasedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
@RestController
@RequestMapping("/api/v1/user/welcome")
@SecurityRequirement(name = "bearerAuth")
public class WelcomeController {

  @Autowired
  private AuthenticationClient authenticationClient;
  
  @Autowired
  private UserService userService;

  @Operation(summary = "Welcome message for user with USER_ROLE")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns a welcome message", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/role/user")
  public ResponseEntity<SingleMessageDTO> welcomeUser() {
    log.info("welcomeUser()");
    User signedInUser = authenticationClient.getSignedInUser();
    String roles = userService.getAllRolesString(signedInUser.getId());

    SingleMessageDTO msgDTO = new SingleMessageDTO();
    msgDTO.setMessage("Welcome user " + signedInUser.getUsername() + ", your roles are: [" + roles + "]!");

    return ResponseEntity.ok(msgDTO);
  }

  @Operation(summary = "Welcome message for user with BROKER_ROLE")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns welcome message", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = SingleMessageDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @GetMapping("/role/broker")
  public ResponseEntity<SingleMessageDTO> welcomeBroker() {
    log.info("welcomeBroker()");
    User signedInUser = authenticationClient.getSignedInUser();
    String roles = userService.getAllRolesString(signedInUser.getId());

    SingleMessageDTO msgDTO = new SingleMessageDTO();
    msgDTO.setMessage("Welcome broker " + signedInUser.getUsername() + ", your roles are: [" + roles + "]!");

    return ResponseEntity.ok(msgDTO);
  }

}

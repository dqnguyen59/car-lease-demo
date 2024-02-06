package org.smartblackbox.carleasedemo.controller;

import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.smartblackbox.carleasedemo.datamodel.dto.AddUserDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.UpdateUserDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.response.UserDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
import org.smartblackbox.carleasedemo.repository.RoleRepository;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.smartblackbox.carleasedemo.service.UserService;
import org.smartblackbox.carleasedemo.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  @Autowired
  private AuthenticationClient authenticationClient;
  
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private UserService userService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Operation(summary = "Add user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User added",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request", 
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @PostMapping("/add")
  public ResponseEntity<UserDTO> addUser(@RequestBody AddUserDTO addUserDTO) {
    Role role = roleRepository.findByRole(RoleType.ROLE_USER).get();
    User registeredUser = userService.addUser(addUserDTO, role);
    UserDTO userDTO = modelMapper.map(registeredUser, UserDTO.class);
    return ResponseEntity.ok(userDTO);
  }

  @Operation(summary = "Find user by Id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns user by Id", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable("id") int id) {
    UserDTO userDTO = modelMapper.map(userService.findById(id).get(), UserDTO.class);
    return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
  }

  @Operation(summary = "Get my account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns the current signed in user", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",  
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/myaccount")
  public ResponseEntity<UserDTO> myUserAccount() {
    User signedInUser = authenticationClient.getSignedInUser();
    return new ResponseEntity<>(modelMapper.map(signedInUser, UserDTO.class), HttpStatus.OK);
  }

  @Operation(summary = "Update user by id")
  /**
   * All fields of updateUserDTO are optional.
   * If a field is not included, then it will not be updated.
   * 
   * The logged in user can only modify their own account or users that have a lower role level.
   * 
   * @param id
   * @param updateUserDTO
   * @return
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns the updated user.", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable("id") int id, @RequestBody UpdateUserDTO updateUserDTO) {
    User signedInUser = authenticationClient.getSignedInUser();
    Optional<User> user = userService.findById(id);

    int loggedInUserHighestRoleLevel = signedInUser.getHighestRoleLevel();
    int userHighestRoleLevel = user.get().getHighestRoleLevel();

    boolean isRoot = signedInUser.hasRole(RoleType.ROLE_ROOT);

    if (signedInUser.getId() != id && loggedInUserHighestRoleLevel <= userHighestRoleLevel) {
      throw new AccessDeniedException(
          "You are not allowed to alter users with a higher or the same role level then yours!");
    }

    if (updateUserDTO.getRoles() != null && signedInUser.getId() == id &&
        !signedInUser.roleMatched(updateUserDTO.getRoles())) {
      throw new AccessDeniedException("You are not allowed to modify roles of your own!");
    }

    return user.map(savedUser -> {
      if (updateUserDTO.getUsername() != null) {
        if (isRoot) {
          throw new AccessDeniedException("You are not allowed to alter root username!");
        }
        savedUser.setUsername(updateUserDTO.getUsername());
      }

      // If user change their own password, then old password must match the current password
      if (signedInUser.getId() == id) {
        if (updateUserDTO.getOldPassword() != null && updateUserDTO.getNewPassword() != null) {
          if (passwordEncoder.matches(updateUserDTO.getOldPassword(), user.get().getPassword())) {
            savedUser.setPassword(passwordEncoder.encode(updateUserDTO.getNewPassword()));
          }
          else {
            throw new BadCredentialsException("Error: old password not match! Password not updated for user id: " + id);
          }
        }
      }
      // Altering other users with a lower role level.
      else if (updateUserDTO.getNewPassword() != null) {
        savedUser.setPassword(passwordEncoder.encode(updateUserDTO.getNewPassword()));
      }

      if (updateUserDTO.getResetPasswordRequired() != null) {
        savedUser.setResetPasswordRequired(updateUserDTO.getResetPasswordRequired());
      }

      if (updateUserDTO.getActive() != null) {
        savedUser.setActive(updateUserDTO.getActive());
      }
      if (updateUserDTO.getEmail() != null) {
        savedUser.setEmail(updateUserDTO.getEmail());
      }

      if (updateUserDTO.getRoles() != null) {
        List<Role> roles = User.validateRoles(loggedInUserHighestRoleLevel, updateUserDTO.getRoles());
        if (roles != null) {
          savedUser.setRoles(roles);
        }
        else {
          throw new AccessDeniedException("You are not allowed to give this user a higher or the same level as yours!");
        }
      }

      User updatedUser = userService.save(savedUser);

      UserDTO userDTO = modelMapper.map(updatedUser, UserDTO.class);

      return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * 
   * @param id
   */
  @Operation(summary = "Delete user by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @ResponseStatus(HttpStatus.NO_CONTENT) // 204
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable("id") int id) {
    User signedInUser = authenticationClient.getSignedInUser();

    if (signedInUser.getId() == id) {
      throw new AccessDeniedException("You are not allowed to remove your own account!");
    }

    Optional<User> user = userService.findById(id);
    int loggedInUserHighestRoleLevel = signedInUser.getHighestRoleLevel();
    int userHighestRoleLevel = user.get().getHighestRoleLevel();

    if (loggedInUserHighestRoleLevel <= userHighestRoleLevel) {
      throw new AccessDeniedException(
          "You are not allowed to remove accounts of others "
              + "that has role levels that is higher then yours!");
    }

    userService.deleteById(id);
  }

  /**
   * 
   * @param pageNo
   * @param pageSize
   * @param sortBy
   * @return
   */
  @Operation(summary = "Gets user list and sort by field")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns a list of sorted users.", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @GetMapping("/page")
  public ResponseEntity<List<UserDTO>> getUserListAndSortBy(
      @RequestParam(name = "pageNo", required = false) Integer pageNo,
      @RequestParam(name = "pageSize", required = false) Integer pageSize,
      @RequestParam(name = "sortBy", required = false) String sortBy) {

    List<User> users = userService.getPage(pageNo, pageSize, sortBy);
    List<UserDTO> userList = SpringUtils.mapList(modelMapper, users, UserDTO.class);

    return new ResponseEntity<List<UserDTO>>(userList, new HttpHeaders(), HttpStatus.OK);
  }	

}

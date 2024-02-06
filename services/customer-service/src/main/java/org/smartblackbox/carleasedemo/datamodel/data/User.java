package org.smartblackbox.carleasedemo.datamodel.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User class is used to store username and user roles.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "User Data")
@Data
public class User implements UserDetails {

  private static final long serialVersionUID = -263192607673244695L;

  /**
   * Field identity.
   */
  @JsonProperty
  private int id;

  /**
   * Field username.
   */
  @JsonProperty
  private String username;

  /**
   * Field password.
   */
  @JsonProperty
  private String password;

  /**
   * Field isTokenValid.
   * Is true when a token is validated via user-service.
   */
  @JsonProperty
  private boolean isTokenValid;

  /**
   * Field roles.
   * Roles are populated when successfully validating token at user-service. 
   */
  @JsonProperty
  private List<Role> roles;
  
  /**
   * This is a constructor to create a user object.
   * 
   * @param username the username that was retrieved from the token 
   * @param password the password of the user account
   * @param isTokenValid determines if the token is valid
   * @param roles the roles this user have to access endpoints
   */
  public User(String username, String password, Boolean isTokenValid, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.isTokenValid = isTokenValid;
    this.roles = roles;
  }
  
  public boolean hasRole(RoleType role) {
    for (Role r : roles) {
      if (r.getType() == role)
        return true;
    }
    return false;
  }

  public boolean roleMatched(List<Role> roles) {
    if (this.roles.size() == roles.size()) {
      for (Role role : roles) {
        if (!this.roles.stream().anyMatch(r -> r.getType().equals(role.getType()))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public String getRolesAssString() {
    return roles.stream().map(Role::getTypeAsString).collect(Collectors.joining(","));
  }

  public int getHighestRoleLevel() {
    int result = 0;
    for (Role role : roles) {
      if (role.getType().getLevel() > result) result = role.getType().getLevel();
    }
    return result;
  }

  public RoleType getHighestRole() {
    return RoleType.byLevel(getHighestRoleLevel());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role: roles) {
      authorities.add(new SimpleGrantedAuthority(role.getType().getName()));
    }
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}

package org.smartblackbox.carleasedemo.datamodel.dto.response;

import java.util.List;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This DTO is used when validating a token from the user-service.
 * The validation will provide the userId and the user roles from the given token.
 * If the token is valid then isTokenValid will be true.
 * The message provides valuable information about the validation.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "Token Response DTO")
@Data
public class TokenResponseDTO {

  /**
   * The id of the user.
   */
  @JsonProperty
  private int userId;

  /**
   * The roles that beints to userId.
   */
  @JsonProperty
  private List<Role> roles;

  /**
   * The message provides valuable information about the validation.
   */
  @JsonProperty
  private String message;

  /**
   * Field isTokenValid
   */
  @JsonProperty
  private boolean isTokenValid;

  /**
   * This is a constructor to create a TokenResponseDTO object.
   * 
   * @param userId the id of the user
   * @param roles the roles that beints to userId
   * @param message the message provides valuable information about the validation
   * @param isTokenValid determines if the token is valid
   */
  public TokenResponseDTO(int userId, List<Role> roles, String message, boolean isTokenValid) {
    this.userId = userId;
    this.roles = roles;
    this.message = message;
    this.isTokenValid = isTokenValid;
  }
  
}

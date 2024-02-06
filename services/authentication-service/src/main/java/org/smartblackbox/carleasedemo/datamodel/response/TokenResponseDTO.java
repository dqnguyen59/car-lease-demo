package org.smartblackbox.carleasedemo.datamodel.response;

import java.util.List;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Schema(description = "Token Response DTO")
@Data
public class TokenResponseDTO {

  @JsonProperty
  private long userId;

  @JsonProperty
  private List<Role> roles;

  @JsonProperty
  private String message;

  @JsonProperty
  private boolean isTokenValid;

}

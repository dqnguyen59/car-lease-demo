package org.smartblackbox.carleasedemo.datamodel.data;

import org.smartblackbox.carleasedemo.datamodel.data.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role class defines the roles a user can have.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "Role")
@Data
public class Role {

  /**
   * Field identity.
   */
  @JsonProperty
  private Integer id;

  /**
   * Field Type.
   */
  @JsonProperty
  private RoleType type;

  /**
   * A constructor to create a Role object.
   * 
   * @param id the identity of the role
   * @param type the type of the role
   */
  public Role(int id, RoleType type) {
    this.id = id;
    this.type = type;
  }
  
  /**
   * Gets the role type as string.
   *  
   * @return role type as string.
   */
  public String getTypeAsString() {
    return type.getName();
  }

}

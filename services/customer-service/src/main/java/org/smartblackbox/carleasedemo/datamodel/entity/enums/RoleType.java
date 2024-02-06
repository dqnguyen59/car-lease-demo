package org.smartblackbox.carleasedemo.datamodel.entity.enums;

/**
 * Enumeration RoleType.
 * A user can have one or more of these roles.
 * The roles are provided with permission level.
 * The higher the level the more permissions the user will have.
 * 
 * ROLE_USER have level 0.
 * ROLE_BROKER have level 9.
 * ROLE_ROOT have level 10.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org> and contributor
 *
 */
public enum RoleType {

  /**
   * ROLE_ROOT have level 10.
   */
  ROLE_ROOT(10, "ROLE_ROOT"),
  
  /**
   * ROLE_BROKER have level 9.
   */
  ROLE_BROKER(9, "ROLE_BROKER"),
  
  /**
   * ROLE_USER have level 0.
   */
  ROLE_USER(0, "ROLE_USER");

  int roleLevel;
  String roleName;

  /**
   * A constructor to create a role type.
   * @param roleLevel the level of the role type
   * @param roleName the name of the role type
   */
  RoleType(int roleLevel, String roleName) {
    this.roleLevel = roleLevel;
    this.roleName = roleName;
  }

  /**
   * The level of the role type.
   * 
   * @return the role level
   */
  public int getLevel() {
    return roleLevel;
  }

  /**
   * The name of the role type.
   * 
   * @return the role name
   */
  public String getName() {
    return roleName;
  }

  /**
   * Gets role type by level
   * 
   * @param roleLevel the level
   * @return the role type
   */
  public static RoleType byLevel(int roleLevel) {
    for (RoleType role : RoleType.values()) {
      if (role.roleLevel == roleLevel) {
        return role;
      }
    }
    return null;
  }

}

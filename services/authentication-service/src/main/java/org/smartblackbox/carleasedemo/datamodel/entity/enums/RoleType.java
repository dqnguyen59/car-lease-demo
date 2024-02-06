package org.smartblackbox.carleasedemo.datamodel.entity.enums;

public enum RoleType {
  ROLE_ROOT(10, "ROLE_ROOT"),
  ROLE_BROKER(9, "ROLE_BROKER"),
  ROLE_USER(0, "ROLE_USER");

  int roleLevel;
  String roleName;

  RoleType(int roleLevel, String roleName) {
    this.roleLevel = roleLevel;
    this.roleName = roleName;
  }

  public int getLevel() {
    return roleLevel;
  }

  public String getName() {
    return roleName;
  }

  public static RoleType byLevel(int roleLevel) {
    for (RoleType role : RoleType.values()) {
      if (role.roleLevel == roleLevel) {
        return role;
      }
    }
    return null;
  }

}

package org.smartblackbox.carleasedemo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.config.ServerConfig;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
import org.smartblackbox.carleasedemo.repository.RoleRepository;
import org.smartblackbox.carleasedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Configuration
@Component
public class InitServerContext {

  @Autowired
  private ServerConfig serverConfig;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    log.info("init()");

    Role roleRoot;
    Role roleBroker;
    Role roleUser;
    Optional<Role> role;

    role = roleRepository.findByRole(RoleType.ROLE_ROOT);
    if (role.isEmpty()) {
      roleRoot = new Role();
      roleRoot.setType(RoleType.ROLE_ROOT);
      roleRepository.save(roleRoot);
    }
    else {
      roleRoot = role.get();
    }

    role = roleRepository.findByRole(RoleType.ROLE_BROKER);
    if (role.isEmpty()) {
      roleBroker = new Role();
      roleBroker.setType(RoleType.ROLE_BROKER);
      roleRepository.save(roleBroker);
    }
    else {
      roleBroker = role.get();
    }

    role = roleRepository.findByRole(RoleType.ROLE_USER);
    if (role.isEmpty()) {
      roleUser = new Role();
      roleUser.setType(RoleType.ROLE_USER);
      roleRepository.save(roleUser);
    }
    else {
      roleUser = role.get();
    }

    String username = serverConfig.rootUsername;
    String password = serverConfig.rootPassword;

    Optional<User> rootUser = userService.findByUsername(username);


    if (rootUser.isEmpty()) {

      User user = User.builder()
          .username(username)
          .password(passwordEncoder.encode(password))
          .resetPasswordRequired(true)
          .build();

      List<Role> roles = Arrays.asList(roleRoot, roleBroker, roleUser);

      user.setRoles(roles);
      user.setActive(true);

      User savedUser = userService.save(user);
      if (savedUser != null) {
        log.info("Root created!");
      }
    }
    else {
      log.info("Root already created!");
    }

    log.info("Root username: " + username);
    log.info("Root password: " + password);
  }

}

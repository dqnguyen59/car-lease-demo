package org.smartblackbox.carleasedemo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.dto.AddUserDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.ResetPasswordDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Service
public class UserService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  public User addUser(AddUserDTO input, Role role) {
    User user = User.builder()
        .username(input.getUsername())
        .password(passwordEncoder.encode(input.getPassword()))
        .active(true)
        .resetPasswordRequired(true)
        .roles(Collections.singletonList(role))
        .build();
    
    return userRepository.save(user);
  }
  
  /**
   * If pageNo is null, then default page 0 is used.
   * If pageSize is null, then default value Integer.MAX_VALUE is used.
   * If sortBy is null, then sorting will not be performed.
   * 
   * @param pageNo
   * @param pageSize
   * @param sortBy
   * @return
   */
  public List<User> getPage(Integer pageNo, Integer pageSize, String sortBy) {
    if (pageNo == null) pageNo = 0;
    if (pageSize == null) pageSize = Integer.MAX_VALUE;

    Pageable paging;
    if (sortBy == null || sortBy.isEmpty()) {
      paging = PageRequest.of(pageNo, pageSize);
    }
    else {
      paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    Page<User> pagedResult = userRepository.findAll(paging);

    if (pagedResult.hasContent()) {
      return pagedResult.getContent();
    }
    return new ArrayList<User>();
  }

  public Optional<User> findById(int id) {
    return userRepository.findById(id);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void deleteById(int id) {
    userRepository.deleteById(id);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUserName(username);
  }

  public Boolean isActive(int id) {
    User user = findById(id).orElseThrow();
    return user.isActive();
  }

  public boolean resetPassword(int id, ResetPasswordDTO resetPasswrodDTO) {
    User user = findById(id).orElseThrow();

    if (user.getPassword() == resetPasswrodDTO.getOldPassword()) {
      user.setPassword(resetPasswrodDTO.getNewPassword());
      userRepository.saveAndFlush(user);
      return true;
    }

    return false;
  }

  public String getAllRolesString(int id) {
    String result = "";
    List<Role> roles = userRepository.getAllRoles(id);

    for (Role role : roles) {
      result += (result.isEmpty()? "" : ", ") + role.getType().getName(); 
    }
    return result;
  }

}

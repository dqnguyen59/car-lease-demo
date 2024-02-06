package org.smartblackbox.carleasedemo.repository;

import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM User u WHERE u.id = :id")
  public Optional<User> findById(@Param("id") int id);

  @Query("SELECT u FROM User u WHERE u.username = :username")
  public Optional<User> findByUserName(@Param("username") String username);

  @Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
  public boolean existsByUserName(@Param("username") String username);

  @Query("SELECT CASE WHEN COUNT(u.email) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
  public boolean existsByEmail(@Param("email") String email);

  @Query("SELECT u.roles FROM User u WHERE u.id = :id")
  public List<Role> getAllRoles(@Param("id") int id);

  //  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.token = :token")
  //  public boolean isTokenValid(@Param("token") String token);


}

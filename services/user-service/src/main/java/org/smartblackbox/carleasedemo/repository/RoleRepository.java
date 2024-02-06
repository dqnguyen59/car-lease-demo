package org.smartblackbox.carleasedemo.repository;

import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
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
public interface RoleRepository extends JpaRepository<Role, Integer> {

  public List<Role> findAll();

  @Query("SELECT r FROM Role r ORDER BY r.type")
  public List<Role> findAllSorted();

  @Query("SELECT u FROM User u WHERE u.id = :id")
  public Optional<Role> findById(@Param("id") int id);

  @Query("SELECT r FROM Role r WHERE r.type = :type")
  public Optional<Role> findByRole(@Param("type") RoleType type);

}

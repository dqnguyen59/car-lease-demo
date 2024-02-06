package org.smartblackbox.carleasedemo.repository;

import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.UserLeaseCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The repository of entity UserLeaseCar.
 * 
 * @author Copyright (C) 2024 Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Repository
public interface UserLeaseCarRepository extends JpaRepository<UserLeaseCar, Integer> {

  /**
   * Find the UserLeaseCar by id.
   * 
   * @param id the id of the car
   * @return the car
   */
  @Query("SELECT c FROM UserLeaseCar c WHERE c.id = :id")
  public Optional<UserLeaseCar> findById(@Param("id") int id);

  /**
   * Find the UserLeaseCar by userId.
   * 
   * @param userId the userId of the UserLeaseCar
   * @return the UserLeaseCar
   */
  @Query("SELECT c FROM UserLeaseCar c WHERE c.userId = :userId")
  public Optional<UserLeaseCar> findByUserId(@Param("userId") int userId);

}

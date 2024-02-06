package org.smartblackbox.carleasedemo.repository;

import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The repository of entity car.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

  /**
   * Find the car by id.
   * 
   * @param id the id of the car
   * @return the car
   */
  @Query("SELECT c FROM Car c WHERE c.id = :id")
  public Optional<Car> findById(@Param("id") int id);

  /**
   * Find the car by make.
   * 
   * @param make the make of the car
   * @return the car
   */
  @Query("SELECT c FROM Car c WHERE c.make = :make")
  public List<Car> findByMake(@Param("make") String make);

}

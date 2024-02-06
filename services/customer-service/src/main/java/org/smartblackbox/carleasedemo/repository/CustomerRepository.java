package org.smartblackbox.carleasedemo.repository;

import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The repository of entity customer.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

  /**
   * Find the customer by id.
   * 
   * @param id the id of the customer
   * @return the customer
   */
  @Query("SELECT c FROM Customer c WHERE c.id = :id")
  public Optional<Customer> findById(@Param("id") int id);

  /**
   * Find the customer by user id.
   * 
   * @param id the id of the user
   * @return the customer
   */
  @Query("SELECT c FROM Customer c WHERE c.userId = :id")
  public Optional<Customer> findByUserId(@Param("id") int id);

  /**
   * Find the customer by name.
   * 
   * @param make the make of the customer
   * @return the customer
   */
  @Query("SELECT c FROM Customer c WHERE c.name = :name")
  public List<Customer> findByName(@Param("name") String name);

}

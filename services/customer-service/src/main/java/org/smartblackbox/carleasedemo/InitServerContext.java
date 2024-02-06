package org.smartblackbox.carleasedemo;

import org.smartblackbox.carleasedemo.datamodel.entity.Customer;
import org.smartblackbox.carleasedemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Initialize the database with some Customers.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Component
public class InitServerContext {

  /**
   * The customer service
   */
  @Autowired
  private CustomerService customerService;

  /**
   * A constructor to create a InitServerContext.
   * 
   */
  public InitServerContext() {

  }

  /**
   * This method is called when the application started and fills the database with some customers.
   * 
   */
  @PostConstruct
  public void init() {
    log.info("init()");

    Customer customer = Customer.builder()
        .userId(1)
        .name("Duy Quoc Nguyen")
        .street("Grotestraat")
        .houseNumber("33B")
        .zipCode("1000 AA")
        .place("Utrecht")
        .emailAddress("peter@mail.com")
        .phoneNumber("+31 (0)6 1324 56789")
        .build();
    customerService.save(customer);

  }

}

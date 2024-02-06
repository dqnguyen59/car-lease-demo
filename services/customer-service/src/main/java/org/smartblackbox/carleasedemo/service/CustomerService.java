package org.smartblackbox.carleasedemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Customer;
import org.smartblackbox.carleasedemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The service of entity customer.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  /**
   * A constructor to create a CustomerService.
   * 
   */
  public CustomerService() {
    
  }
  
  /**
   * If pageNo is null, then default page 0 is used.
   * If pageSize is null, then default value Integer.MAX_VALUE is used.
   * If sortBy is null, then sorting will not be performed.
   * 
   * @param pageNo the page number starting from 0 
   * @param pageSize the page size 
   * @param sortBy the sort by the given field
   * @return the list of customers
   */
  public List<Customer> getPage(Integer pageNo, Integer pageSize, String sortBy) {
    if (pageNo == null) pageNo = 0;
    if (pageSize == null) pageSize = Integer.MAX_VALUE;

    Pageable paging;
    if (sortBy == null || sortBy.isEmpty()) {
      paging = PageRequest.of(pageNo, pageSize);
    }
    else {
      paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    Page<Customer> pagedResult = customerRepository.findAll(paging);

    if (pagedResult.hasContent()) {
      return pagedResult.getContent();
    }
    return new ArrayList<Customer>();
  }

  /**
   * Find the customer by id.
   * 
   * @param id the id of the customer
   * @return the customer
   */
  public Optional<Customer> findById(int id) {
    return customerRepository.findById(id);
  }

  /**
   * Find the customer by user id.
   * 
   * @param id the id of the user
   * @return the customer
   */
  public Optional<Customer> findByUserId(int id) {
    return customerRepository.findByUserId(id);
  }

  /**
   * Find the customer by the make of the customer.
   * 
   * @param name the id of the customer
   * @return the list of found customers
   */
  public List<Customer> findByName(String name) {
    return customerRepository.findByName(name);
  }

  
  /**
   * Saves the customer.
   * 
   * @param Customer the customer
   * @return the saved customer
   */
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  /**
   * Deletes the customer by id.
   * 
   * @param id the id of the customer
   */
  public void deleteById(int id) {
    customerRepository.deleteById(id);
  }

}

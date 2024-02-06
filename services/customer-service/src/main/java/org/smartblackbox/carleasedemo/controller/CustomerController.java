package org.smartblackbox.carleasedemo.controller;

import java.util.List;
import java.util.Optional;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.smartblackbox.carleasedemo.datamodel.data.User;
import org.smartblackbox.carleasedemo.datamodel.dto.CustomerDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Customer;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.smartblackbox.carleasedemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

/**
 * The Rest controller of entity Customer.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

  @Autowired
  private AuthenticationClient authenticationClient;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * A constructor to create a CustomerController.
   * 
   */
  public CustomerController() {
    
  }
  
  /**
   * Add the customer to the database.
   * 
   * @param Customer the customer
   * @return the customer
   */
  @Operation(summary = "Add customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Customer added",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request", 
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @PostMapping("/add")
  public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDTO customerDTO) {
    Customer customer = modelMapper.map(customerDTO, Customer.class);
    Customer registeredCustomer = customerService.save(customer);
    return ResponseEntity.ok(registeredCustomer);
  }

  /**
   * Updates the customer.
   * 
   * All fields of updateCustomerDTO are optional.
   * If a field is not included, then it will not be updated.
   * 
   * @param id the id of the customer
   * @param customerDTO the customer
   * @return the customer
   */
  @Operation(summary = "Update customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Customer updated", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(@PathVariable("id") int id, @RequestBody CustomerDTO customerDTO) {
    User signedInUser = authenticationClient.getSignedInUser();
    
    Optional<Customer> customer = customerService.findById(id);

    // If the signed in user has a broker role, then the signed in user can modify customer details of others
    // that has a users that has a ROLE_USER (TODO: Get roles from from user by id 
    // via user-service and add to the condition). 
    // Otherwise, the user can only modify their own customer details.
    if (customer.isPresent() && (signedInUser.hasRole(RoleType.ROLE_BROKER) || signedInUser.getId() == customer.get().getUserId())) {
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      modelMapper.map(customerDTO, customer.get());
      customerService.save(customer.get());
    }
    else {
      throw new AccessDeniedException("You are not allowed to modify customer details of other users!");
    }
    return ResponseEntity.ok(customer.get());
  }

  /**
   * Find the customer by id.
   * 
   * @param id the id of the customer
   * @return customer
   */
  @Operation(summary = "Find customer by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns customer by Id", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @GetMapping("/{id}")
  public ResponseEntity<Customer> findById(@PathVariable("id") int id) {
    Customer customer = customerService.findById(id).get();
    return new ResponseEntity<Customer>(customer, HttpStatus.OK);
  }

  /**
   * Gets my account customer info.
   * 
   * @param id the id of the customer
   * @return customer
   */
  @Operation(summary = "Get my customer info")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns customer by Id", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/myinfo")
  public ResponseEntity<Customer> myInfo() {
    int id = authenticationClient.getSignedInUser().getId();
    Customer customer = customerService.findByUserId(id).get();
    return new ResponseEntity<Customer>(customer, HttpStatus.OK);
  }

  /**
   * Deletes the customer by id.
   * 
   * @param id the id of the customer
   */
  @Operation(summary = "Delete customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Delete customer", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }), 
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @ResponseStatus(HttpStatus.NO_CONTENT) // 204
  @DeleteMapping("/{id}")
  public void deleteCustomer(@PathVariable("id") int id) {
    customerService.deleteById(id);
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
  @Operation(summary = "Get customer list")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns list", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @GetMapping("/page")
  public ResponseEntity<List<Customer>> getCustomerListAndSortBy(
      @RequestParam(name = "pageNo", required = false) Integer pageNo,
      @RequestParam(name = "pageSize", required = false) Integer pageSize,
      @RequestParam(name = "sortBy", required = false) String sortBy) {

    List<Customer> customers = customerService.getPage(pageNo, pageSize, sortBy);

    return new ResponseEntity<List<Customer>>(customers, new HttpHeaders(), HttpStatus.OK);
  }	

}

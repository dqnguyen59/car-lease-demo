package org.smartblackbox.carleasedemo.datamodel.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Entity customer.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@ToString
@NoArgsConstructor
@Builder
@Schema(description = "Customer")
@Data
@Entity
@Table(name = "Customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer userId;
  
  @NotBlank
  private String name;

  @NotBlank
  private String street;

  @NotBlank
  private String houseNumber;

  @NotBlank
  private String zipCode;

  @NotBlank
  private String place;

  @NotBlank
  private String emailAddress;

  private String phoneNumber;

  /**
   * A constructor to create a customer.
   * 
   * @param id the identity of the customer
   * @param name the name of the customer
   * @param street the street of the customer
   * @param houseNumber the houseNumber of the customer
   * @param zipCode the zipCode of the customer
   * @param place the place of the customer
   * @param emailAddress the emailAddress of the customer
   * @param phoneNumber the phoneNumber of the customer
   */
  public Customer(Integer id, Integer userId, @NotBlank String name, @NotBlank String street,
      @NotBlank String houseNumber, @NotBlank String zipCode, @NotBlank String place,
      @NotBlank String emailAddress, String phoneNumber) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.street = street;
    this.houseNumber = houseNumber;
    this.zipCode = zipCode;
    this.place = place;
    this.emailAddress = emailAddress;
    this.phoneNumber = phoneNumber;
  }

}

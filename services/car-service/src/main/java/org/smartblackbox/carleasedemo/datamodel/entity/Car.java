package org.smartblackbox.carleasedemo.datamodel.entity;

import java.util.Date;
import org.hibernate.annotations.Formula;
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

/**
 * The Entity Car.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@NoArgsConstructor
@Builder
@Schema(description = "Car")
@Data
@Entity
@Table(name = "Cars")
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  private String make;

  @NotBlank
  private String model;

  private String version;

  private Integer numberOfDoors;

  private Integer cO2Emission;

  private Double grossPrice;

  private Double nettPrice;

  private Integer mileage;

  private double interestRate;

  private Date startDate;

  private Integer duration;

  @Formula("((( MILEAGE / 12 ) * DURATION ) / NETT_PRICE) + ((( INTEREST_RATE / 100 ) * NETT_PRICE) / 12)")
  private Double leaseRate;

  /**
   * A constructor to create a Car.
   * 
   * @param id the identity of the car
   * @param make the make of the car
   * @param model the model of the car
   * @param version the version of the car
   * @param numberOfDoors the number of car doors
   * @param cO2Emission the cO2Emission of the car
   * @param grossPrice the grossPrice of the car
   * @param nettPrice the nettPrice of the car
   * @param mileage the mileage of the car
   * @param interestRate the interestRate of the car
   * @param startDate the interest date of the car
   * @param duration the lease duration of the car
   * @param leaseRate the total price of the car
   */
  public Car(Integer id, @NotBlank String make, @NotBlank String model, String version,
      Integer numberOfDoors, Integer cO2Emission, Double grossPrice, Double nettPrice,
      Integer mileage, double interestRate, Date startDate, Integer duration, Double leaseRate) {
    super();
    this.id = id;
    this.make = make;
    this.model = model;
    this.version = version;
    this.numberOfDoors = numberOfDoors;
    this.cO2Emission = cO2Emission;
    this.grossPrice = grossPrice;
    this.nettPrice = nettPrice;
    this.mileage = mileage;
    this.interestRate = interestRate;
    this.startDate = startDate;
    this.duration = duration;
    this.leaseRate = leaseRate;
  }


}

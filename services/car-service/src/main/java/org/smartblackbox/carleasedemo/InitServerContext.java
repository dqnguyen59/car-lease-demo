package org.smartblackbox.carleasedemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.smartblackbox.carleasedemo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Initialize the database with some cars.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Component
public class InitServerContext {

  /**
   * The car service
   */
  @Autowired
  private CarService carService;

  /**
   * A constructor to create a InitServerContext.
   * 
   */
  public InitServerContext() {
    
  }
  
  /**
   * This method is called when the application started and fills the database with some cars.
   * 
   */
  @PostConstruct
  public void init() {
    log.info("init()");

    Car car;
    try {
      car = Car.builder()
          .make("BMW")
          .model("5 Serie")
          .version("25th Edition")
          .mileage(45000)
          .duration(60)
          .cO2Emission(10)
          .grossPrice(76230.0)
          .nettPrice(63000.0)
          .numberOfDoors(4)
          .interestRate(4.5)
          .startDate(new SimpleDateFormat("yyyyMMddHH").parse("2024030112"))
          .build();
      carService.save(car);
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

}

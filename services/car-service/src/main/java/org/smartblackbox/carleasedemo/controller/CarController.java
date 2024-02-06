package org.smartblackbox.carleasedemo.controller;

import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.smartblackbox.carleasedemo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * The Rest controller of entity car.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@RestController
@RequestMapping("/api/v1/car")
@SecurityRequirement(name = "bearerAuth")
public class CarController {

  @Autowired
  AuthenticationClient authClient;
  
  @Autowired
  private CarService carService;

  /**
   * A constructor to create a CarController.
   * 
   */
  public CarController() {
    
  }
  
  /**
   * Add the car to the database.
   * 
   * @param car the car
   * @return the car
   */
  @Operation(summary = "Add car")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Car added",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
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
  public ResponseEntity<Car> addCar(@RequestBody Car car) {
    Car registeredCar = carService.save(car);
    return ResponseEntity.ok(registeredCar);
  }

  /**
   * Find the car by id.
   * 
   * @param id the id of the car
   * @return car
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns car by Id", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
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
  @GetMapping("/{id}")
  public ResponseEntity<Car> findById(@PathVariable("id") int id) {
    Car car = carService.findById(id).get();
    return new ResponseEntity<Car>(car, HttpStatus.OK);
  }

  /**
   * Updates the car.
   * 
   * All fields of updateCarDTO are optional.
   * If a field is not included, then it will not be updated.
   * 
   * @param id the id of the car
   * @param updateCar the car
   * @return the car
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Car updated", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
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
  @PutMapping("/{id}")
  public ResponseEntity<Car> updateCar(@PathVariable("id") int id, @RequestBody Car updateCar) {
    Optional<Car> car = carService.findById(id);

    return car.map(savedCar -> {
      if (updateCar.getMake() != null) {
        savedCar.setMake(updateCar.getMake());
      }

      if (updateCar.getModel() != null) {
        savedCar.setModel(updateCar.getModel());
      }

      if (updateCar.getVersion() != null) {
        savedCar.setVersion(updateCar.getVersion());
      }

      if (updateCar.getNumberOfDoors() != null) {
        savedCar.setNumberOfDoors(updateCar.getNumberOfDoors());
      }

      if (updateCar.getCO2Emission() != null) {
        savedCar.setCO2Emission(updateCar.getCO2Emission());
      }

      if (updateCar.getGrossPrice() != null) {
        savedCar.setGrossPrice(updateCar.getGrossPrice());
      }

      if (updateCar.getNettPrice() != null) {
        savedCar.setNettPrice(updateCar.getNettPrice());
      }

      Car updatedCar = carService.save(savedCar);

      return new ResponseEntity<Car>(updatedCar, HttpStatus.OK);
    })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Deletes the car by id.
   * 
   * @param id the id of the car
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Delete car", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
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
  public void deleteCar(@PathVariable("id") int id) {
    carService.deleteById(id);
  }

  /**
   * If pageNo is null, then default page 0 is used.
   * If pageSize is null, then default value Integer.MAX_VALUE is used.
   * If sortBy is null, then sorting will not be performed.
   * 
   * @param pageNo the page number starting from 0 
   * @param pageSize the page size 
   * @param sortBy the sort by the given field
   * @return the list of cars
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns list", 
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
      }),
      @ApiResponse(responseCode = "403", description = "User not authorized",
      content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultExceptionDTO.class))
      }),
  })
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/page")
  public ResponseEntity<List<Car>> getCarListAndSortBy(
      @RequestParam(name = "pageNo", required = false) Integer pageNo,
      @RequestParam(name = "pageSize", required = false) Integer pageSize,
      @RequestParam(name = "sortBy", required = false) String sortBy) {

    List<Car> cars = carService.getPage(pageNo, pageSize, sortBy);

    return new ResponseEntity<List<Car>>(cars, new HttpHeaders(), HttpStatus.OK);
  }	

}

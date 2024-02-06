package org.smartblackbox.carleasedemo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.dto.response.exception.DefaultExceptionDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.smartblackbox.carleasedemo.datamodel.entity.UserLeaseCar;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.LeaseCarStatus;
import org.smartblackbox.carleasedemo.service.AuthenticationClient;
import org.smartblackbox.carleasedemo.service.UserLeaseCarService;
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
 * @author Copyright (C) 2024 Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@RestController
@RequestMapping("/api/v1/userleasecar")
@SecurityRequirement(name = "bearerAuth")
public class UserLeaseCarController {

  @Autowired
  AuthenticationClient authClient;

  @Autowired
  private UserLeaseCarService userLeaseCarService;

  /**
   * A constructor to create a CarController.
   * 
   */
  public UserLeaseCarController() {

  }

  /**
   * User request car model.
   * 
   * @param car the car
   * @return the car
   */
  @Operation(summary = "User request car model")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Car requested",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Car.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),
      @ApiResponse(responseCode = "403", description = "User not authorized",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),})
  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping("/requestForLease/{userId}/{carId}")
  public ResponseEntity<UserLeaseCar> userRequestCar(@PathVariable("userId") int userId,
      @PathVariable("carId") int carId) {
    Optional<UserLeaseCar> foundUserLeaseCar = userLeaseCarService.findByUserId(userId);

    UserLeaseCar userLeaseCar =
        (foundUserLeaseCar.isPresent()) ? foundUserLeaseCar.get() : new UserLeaseCar();

    userLeaseCar.setUserId(userId);
    userLeaseCar.setCarId(carId);
    userLeaseCar.setStatus(LeaseCarStatus.ORDERED);
    userLeaseCar.setLeaseOrderDate(new Date());

    UserLeaseCar registeredUserLeaseCar = userLeaseCarService.save(userLeaseCar);
    return ResponseEntity.ok(registeredUserLeaseCar);
  }

  /**
   * Updates the status user car request.
   * 
   * @param car the car
   * @return the car
   */
  @Operation(summary = "Updates the status user car request")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status updated",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Car.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),
      @ApiResponse(responseCode = "403", description = "User not authorized",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),})
  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping("/requestForLease/{userId}/{status}")
  public ResponseEntity<UserLeaseCar> updateCarUserRequest(@PathVariable("userId") int userId,
      @PathVariable("status") String status) {
    UserLeaseCar userLeaseCar = userLeaseCarService.findByUserId(userId).get();

    userLeaseCar.setStatus(LeaseCarStatus.valueOf(status));
    UserLeaseCar registeredUserLeaseCar = userLeaseCarService.save(userLeaseCar);
    return ResponseEntity.ok(registeredUserLeaseCar);
  }

  /**
   * Find the UserLeaseCar by id.
   * 
   * @param id the id of the UserLeaseCar
   * @return UserLeaseCar
   */
  @Operation(summary = "Find the user request car model by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns car by Id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserLeaseCar.class))}),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),
      @ApiResponse(responseCode = "403", description = "User not authorized",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),})
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/{id}")
  public ResponseEntity<UserLeaseCar> findByUserId(@PathVariable("id") int id) {
    UserLeaseCar userLeaseCar = userLeaseCarService.findByUserId(id).get();
    return new ResponseEntity<UserLeaseCar>(userLeaseCar, HttpStatus.OK);
  }

  /**
   * Delete the car model of user request by id.
   * 
   * @param id the id of the UserLeaseCar
   */
  @Operation(summary = "Delete the car model of user request by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Request deleted",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Car.class))}),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),
      @ApiResponse(responseCode = "403", description = "User not authorized",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),})
  @PreAuthorize("hasRole('ROLE_BROKER')")
  @ResponseStatus(HttpStatus.NO_CONTENT) // 204
  @DeleteMapping("/{id}")
  public void deleteCar(@PathVariable("id") int id) {
    userLeaseCarService.deleteById(id);
  }

  /**
   * If pageNo is null, then default page 0 is used. If pageSize is null, then default value
   * Integer.MAX_VALUE is used. If sortBy is null, then sorting will not be performed.
   * 
   * @param pageNo the page number starting from 0
   * @param pageSize the page size
   * @param sortBy the sort by the given field
   * @return the list of cars
   */
  @Operation(summary = "Show the list of user requests for car models")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns list",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Car.class))}),
      @ApiResponse(responseCode = "403", description = "User not authorized",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = DefaultExceptionDTO.class))}),})
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/page")
  public ResponseEntity<List<UserLeaseCar>> getUserLeaseCarListAndSortBy(
      @RequestParam(name = "pageNo", required = false) Integer pageNo,
      @RequestParam(name = "pageSize", required = false) Integer pageSize,
      @RequestParam(name = "sortBy", required = false) String sortBy) {

    List<UserLeaseCar> userLeaseCarList = userLeaseCarService.getPage(pageNo, pageSize, sortBy);

    return new ResponseEntity<List<UserLeaseCar>>(userLeaseCarList, new HttpHeaders(),
        HttpStatus.OK);
  }

}

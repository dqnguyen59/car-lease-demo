package org.smartblackbox.carleasedemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.smartblackbox.carleasedemo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The service of entity car.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Service
public class CarService {

  @Autowired
  private CarRepository carRepository;

  /**
   * A constructor to create a CarService.
   * 
   */
  public CarService() {
    
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
  public List<Car> getPage(Integer pageNo, Integer pageSize, String sortBy) {
    if (pageNo == null) pageNo = 0;
    if (pageSize == null) pageSize = Integer.MAX_VALUE;

    Pageable paging;
    if (sortBy == null || sortBy.isEmpty()) {
      paging = PageRequest.of(pageNo, pageSize);
    }
    else {
      paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    Page<Car> pagedResult = carRepository.findAll(paging);

    if (pagedResult.hasContent()) {
      return pagedResult.getContent();
    }
    return new ArrayList<Car>();
  }

  /**
   * Find the car by id.
   * 
   * @param id the id of the car
   * @return the car
   */
  public Optional<Car> findById(int id) {
    return carRepository.findById(id);
  }

  /**
   * Find the car by the make of the car.
   * 
   * @param make the id of the car
   * @return the list of found cars
   */
  public List<Car> findByMake(String make) {
    return carRepository.findByMake(make);
  }

  /**
   * Saves the car.
   * 
   * @param car the car
   * @return the saved car
   */
  public Car save(Car car) {
    return carRepository.save(car);
  }

  /**
   * Deletes the car by id.
   * 
   * @param id the id of the car
   */
  public void deleteById(int id) {
    carRepository.deleteById(id);
  }

}

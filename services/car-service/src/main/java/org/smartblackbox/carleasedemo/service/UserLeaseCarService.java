package org.smartblackbox.carleasedemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.smartblackbox.carleasedemo.datamodel.entity.UserLeaseCar;
import org.smartblackbox.carleasedemo.repository.UserLeaseCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The service of entity UserLeaseCarService.
 * 
 * @author Copyright (C) 2024 Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Service
public class UserLeaseCarService {

  @Autowired
  private UserLeaseCarRepository userLeaseCarRepository;

  /**
   * A constructor to create a UserLeaseCarService.
   * 
   */
  public UserLeaseCarService() {

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
  public List<UserLeaseCar> getPage(Integer pageNo, Integer pageSize, String sortBy) {
    if (pageNo == null)
      pageNo = 0;
    if (pageSize == null)
      pageSize = Integer.MAX_VALUE;

    Pageable paging;
    if (sortBy == null || sortBy.isEmpty()) {
      paging = PageRequest.of(pageNo, pageSize);
    } else {
      paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    Page<UserLeaseCar> pagedResult = userLeaseCarRepository.findAll(paging);

    if (pagedResult.hasContent()) {
      return pagedResult.getContent();
    }
    return new ArrayList<UserLeaseCar>();
  }

  /**
   * Find the UserLeaseCar by id.
   * 
   * @param id the id of the UserLeaseCar
   * @return the UserLeaseCar
   */
  public Optional<UserLeaseCar> findById(int id) {
    return userLeaseCarRepository.findById(id);
  }

  /**
   * Find the car by the make of the car.
   * 
   * @param userId the user
   * @return the list of found UserLeaseCar
   */
  public Optional<UserLeaseCar> findByUserId(int userId) {
    return userLeaseCarRepository.findByUserId(userId);
  }

  /**
   * Saves the UserLeaseCar.
   * 
   * @param userLeaseCar the UserLeaseCar
   * @return the saved UserLeaseCar
   */
  public UserLeaseCar save(UserLeaseCar userLeaseCar) {
    return userLeaseCarRepository.save(userLeaseCar);
  }

  /**
   * Deletes the UserLeaseCar by id.
   * 
   * @param id the id of the UserLeaseCar
   */
  public void deleteById(int id) {
    userLeaseCarRepository.deleteById(id);
  }

}

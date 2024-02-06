package org.smartblackbox.carleasedemo.datamodel.entity;

import java.util.Date;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.LeaseCarStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Entity CustomerLeaseCar.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@ToString
@NoArgsConstructor
@Builder
@Schema(description = "User LeaseCar")
@Data
@Entity
@Table() 
public class UserLeaseCar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private Integer userId;
  
  private Integer carId;
  
  private LeaseCarStatus status;

  private Date leaseOrderDate;
  
  private Date leaseStartDate;

  public UserLeaseCar(Integer id, Integer userId, Integer carId,
      LeaseCarStatus status, Date leaseOrderDate, Date leaseStartDate) {
    super();
    this.id = id;
    this.userId = userId;
    this.carId = carId;
    this.status = status;
    this.leaseOrderDate = leaseOrderDate;
    this.leaseStartDate = leaseStartDate;
  }

}

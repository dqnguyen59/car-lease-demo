package org.smartblackbox.carleasedemo.datamodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Schema(description = "Customer DTO")
@Data
public class CustomerDTO {

  @JsonProperty
  private Integer userId;
  
  @JsonProperty
  private String name;

  @JsonProperty
  private String street;

  @JsonProperty
  private String houseNumber;

  @JsonProperty
  private String zipCode;

  @JsonProperty
  private String place;

  @JsonProperty
  private String emailAddress;

  @JsonProperty
  private String phoneNumber;

}

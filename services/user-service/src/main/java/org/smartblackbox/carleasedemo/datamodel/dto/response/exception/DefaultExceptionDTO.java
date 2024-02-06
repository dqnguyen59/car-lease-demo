package org.smartblackbox.carleasedemo.datamodel.dto.response.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This DTO is used for Swagger-UI.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "Default Exception DTO")
@Data
public class DefaultExceptionDTO {

  @JsonProperty
  private String type;

  @JsonProperty
  private String title;

  @JsonProperty
  private int status;

  @JsonProperty
  private String detail;

  @JsonProperty
  private String instance;

  @JsonProperty
  private String description;

  /**
   * A constructor to create a DefaultExceptionDTO.
   * 
   * @param type the parameter type 
   * @param title the parameter title
   * @param status the parameter status 
   * @param detail the parameter detail
   * @param instance the parameter instance
   * @param description the parameter description
   */
  public DefaultExceptionDTO(String type, String title, int status, String detail, String instance,
      String description) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.description = description;
  }


}

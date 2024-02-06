package org.smartblackbox.carleasedemo.datamodel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * This DTO is used to send a single message.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Schema(description = "Single Message DTO")
@Data
public class SingleMessageDTO {

  @JsonProperty
  private String message;
  
  /**
   * A constructor to create a SingleMessageDTO object.
   * 
   */
  public SingleMessageDTO() {
    
  }
}

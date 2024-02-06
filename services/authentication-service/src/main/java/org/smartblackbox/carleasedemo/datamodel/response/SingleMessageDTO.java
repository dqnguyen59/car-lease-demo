package org.smartblackbox.carleasedemo.datamodel.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Schema(description = "Single Message DTO")
@Data
public class SingleMessageDTO {

  @JsonProperty
  private String message;
}

package org.smartblackbox.leasedemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Schema(description = "User DTO")
@Data
public class RoleDTO {

	@JsonProperty
	private Integer id;

	@JsonProperty
	private org.smartblackbox.carleasedemo.datamodel.data.enums.RoleType type;

}

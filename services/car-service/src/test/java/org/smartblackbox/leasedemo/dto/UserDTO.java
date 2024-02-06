package org.smartblackbox.leasedemo.dto;

import java.util.List;
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
public class UserDTO {

	@JsonProperty
	private int id;

	@JsonProperty
	private String username;

	@JsonProperty
	private String email;

	@JsonProperty
	private boolean resetPasswordRequired;
	
	@JsonProperty
	private List<RoleDTO> roles;
	
	@JsonProperty
	private boolean active;

	@JsonProperty
	private String token;
	
	@JsonProperty
	private long expiredTime;
	
}

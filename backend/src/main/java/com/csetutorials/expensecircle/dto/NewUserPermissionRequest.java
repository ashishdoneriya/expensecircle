package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.beans.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserPermissionRequest {

	@NotNull
	@NotBlank
	private String userId;

	@NotNull
	private Role role;

}

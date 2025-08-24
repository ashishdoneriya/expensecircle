package com.csetutorials.expensecircle.beans;

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

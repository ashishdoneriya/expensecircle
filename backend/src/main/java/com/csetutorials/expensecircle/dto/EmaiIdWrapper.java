package com.csetutorials.expensecircle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmaiIdWrapper {

	@NotNull
	@NotBlank
	private String email;

}

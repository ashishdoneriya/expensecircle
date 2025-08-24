package com.csetutorials.expensecircle.beans;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {

	@NotBlank
	private String name;

}

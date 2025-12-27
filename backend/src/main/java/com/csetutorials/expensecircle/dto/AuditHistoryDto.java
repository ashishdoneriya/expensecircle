package com.csetutorials.expensecircle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditHistoryDto {

	private String createdByName;
	private String createdByEmail;
	private Long createdAt;

	private String updatedByName;
	private String updatedByEmail;
	private Long updatedAt;

}

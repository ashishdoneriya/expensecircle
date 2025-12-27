package com.csetutorials.expensecircle.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpenseRequest {

	private long timestamp;

	private long amount;

	private String description;

	private String categoryId;

	private List<ExpenseTagDto> tags; // ids

}

package com.csetutorials.expensecircle.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateExpenseRequest {

	private String expenseId;

	private long amount;

	private String description;

	private String categoryId;

	private long newTimestamp;

	private Set<String> tags;

	private Set<String> newTags;

}

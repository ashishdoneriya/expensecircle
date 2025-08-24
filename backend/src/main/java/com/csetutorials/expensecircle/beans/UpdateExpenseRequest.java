package com.csetutorials.expensecircle.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateExpenseRequest {

	private long expenseId;

	private long amount;

	private String description;

	private long categoryId;

	private long newTimestamp;

	private Set<Long> tags;

	private Set<String> newTags;

}

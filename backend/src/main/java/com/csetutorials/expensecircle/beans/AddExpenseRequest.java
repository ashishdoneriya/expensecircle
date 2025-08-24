package com.csetutorials.expensecircle.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AddExpenseRequest {

	private long timestamp;

	private long amount;

	private String description;

	private long categoryId;

	private Set<Long> tags;

	private Set<String> newTags;

}

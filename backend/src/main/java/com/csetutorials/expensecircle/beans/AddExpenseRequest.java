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

	private String categoryId;

	private Set<String> tags; // ids

	private Set<String> newTags;

}

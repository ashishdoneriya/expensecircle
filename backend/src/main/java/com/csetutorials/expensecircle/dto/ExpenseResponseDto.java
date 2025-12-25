package com.csetutorials.expensecircle.dto;

import java.util.List;

public record ExpenseResponseDto (
	String expenseId,
	long timestamp,
	String categoryId,
	long amount,
	String description,
	List<String> tags,
	String ownerUserId,
	String lastChangedByUserId
) {}

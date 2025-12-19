package com.csetutorials.expensecircle.dto;

import java.util.List;

public record ExpenseResponseDto (
	long expenseId,
	long timestamp,
	long categoryId,
	long amount,
	String description,
	List<Long> tags,
	String ownerUserId,
	String lastChangedByUserId
) {}

package com.csetutorials.expensecircle.dto;

import java.util.List;

public record ExpenseResponseDto (
	String expenseId,
	long timestamp,
	String timezone,
	String categoryId,
	long amount,
	String description,
	List<String> tags,
	String createdBy,
	String updatedBy,
	Long createdAt,
	Long updatedAt
) {}

package com.csetutorials.expensecircle.projection;

public record ExpenseProjection (
	String expenseId,
	long timestamp,
	Long amount,
	String description,
	String categoryId
){}

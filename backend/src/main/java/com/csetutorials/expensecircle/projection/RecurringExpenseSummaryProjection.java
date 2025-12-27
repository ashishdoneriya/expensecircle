package com.csetutorials.expensecircle.projection;

import com.csetutorials.expensecircle.beans.RecurringFrequency;

public record RecurringExpenseSummaryProjection(
	String recurringId,
	long amount,
	String description,
	RecurringFrequency frequency,
	String categoryId
) {}
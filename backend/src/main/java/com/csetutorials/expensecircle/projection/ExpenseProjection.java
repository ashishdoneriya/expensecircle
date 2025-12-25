package com.csetutorials.expensecircle.projection;

public interface ExpenseProjection {

	String getExpenseId();

	long getTimestamp();

	Long getAmount();

	String getDescription();

	String getCategoryId();

}

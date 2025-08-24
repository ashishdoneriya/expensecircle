package com.csetutorials.expensecircle.projection;

public interface ExpenseProjection {

	Long getExpenseId();

	Long getAmount();

	String getDescription();

	Long getCategoryId();

}

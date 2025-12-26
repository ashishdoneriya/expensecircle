package com.csetutorials.expensecircle.projection;

import com.csetutorials.expensecircle.beans.RecurringFrequency;

public interface RecurringExpenseSummaryProjection {

	long getRecurringId();

	long getAmount();

	String getDescription();

	RecurringFrequency getFrequency();

}

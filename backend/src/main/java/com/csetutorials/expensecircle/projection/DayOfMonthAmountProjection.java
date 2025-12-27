package com.csetutorials.expensecircle.projection;

public record DayOfMonthAmountProjection (
	byte dayOfMonth,
	long amount
){}

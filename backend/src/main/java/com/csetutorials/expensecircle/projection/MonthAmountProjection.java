package com.csetutorials.expensecircle.projection;

public record MonthAmountProjection (
	byte month,
	long amount
){}
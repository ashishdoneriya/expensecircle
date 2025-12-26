package com.csetutorials.expensecircle.beans;

import lombok.*;

import java.time.DayOfWeek;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringExpenseRequestDto {

	private long categoryId;
	private long amount;
	private String description;
	private RecurringFrequency frequency;
	private Integer dayOfMonth; // 1-31 or 99 for Last Day
	private DayOfWeek dayOfWeek;
	private byte executionTimeHour; // 0-23
	private byte executionTimeMinute; // 0-59

}

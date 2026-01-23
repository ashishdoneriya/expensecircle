package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.beans.DayOfWeek;
import com.csetutorials.expensecircle.beans.DayPeriod;
import com.csetutorials.expensecircle.beans.RecurringFrequency;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringExpenseRequest {

	private String categoryId;
	private List<ExpenseTagDto> tags;
	private long amount;
	private String description;

	@NotNull
	@NotBlank
	private RecurringFrequency frequency;

	@Min(1)
	@Max(99)
	private Byte dayOfMonth; // 1-31 or 99 for Last Day
	private DayOfWeek dayOfWeek;

	@Min(value = 0, message = "hour must be b/w 0 and 23")
	@Max(value = 23, message = "hour must be b/w 0 and 23")
	private byte executionTimeHour; // 0-11

	@Min(value = 0, message = "minute must be b/w 0 and 59")
	@Max(value = 59, message =  "minute must be b/w 0 and 59")
	private byte executionTimeMinute;

	private DayPeriod dayPeriod;

	@NotNull
	private String timezone;

}
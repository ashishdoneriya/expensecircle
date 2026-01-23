package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.beans.DayOfWeek;
import com.csetutorials.expensecircle.beans.DayPeriod;
import com.csetutorials.expensecircle.beans.RecurringFrequency;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringExpenseDetailResponseDto {

	private String recurringId;
	private String categoryId;
	private List<String> tags;
	private long amount;
	private String description;
	private RecurringFrequency frequency;
	private Byte dayOfMonth; // 1-31 or 99 for Last Day
	private DayOfWeek dayOfWeek;
	private byte executionTimeHour; // 0-23
	private byte executionTimeMinute; // 0-59
	private DayPeriod dayPeriod;
	private String timezone;
	private String createdBy;
	private String updatedBy;
	private Long createdAt;
	private Long updatedAt;

}

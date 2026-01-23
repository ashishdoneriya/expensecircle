package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.beans.DayOfWeek;
import com.csetutorials.expensecircle.beans.DayPeriod;
import com.csetutorials.expensecircle.beans.RecurringFrequency;
import com.csetutorials.expensecircle.entities.ids.RecurringExpenseId;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recurringExpenses")
@IdClass(RecurringExpenseId.class)
@Builder
public class RecurringExpense extends Auditable {

	@Id
	private String groupId;

	@Id
	private String recurringId;

	@Column
	private String categoryId;

	@Column
	private long amount;

	@Column
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RecurringFrequency frequency;

	@Column
	private byte dayOfMonth; // 1-31 or 99 for Last Day

	@Column(columnDefinition = "CHAR(3)")
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@Column
	private byte executionTimeHour; // 0-23

	@Column
	private byte executionTimeMinute; // 0-59

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(2)")
	private DayPeriod dayPeriod;

	@Column(nullable = false, length = 35)
	private String timezone;

	@Column
	private long nextTriggerEpoch;

}
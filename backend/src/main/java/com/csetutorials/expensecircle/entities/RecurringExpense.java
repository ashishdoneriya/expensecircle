package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.beans.RecurringFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recurringExpenses")
@IdClass(RecurringExpense.class)
public class RecurringExpense {

	@Id
	private Long groupId;

	@Id
	private Long recurringId;

	@Column
	private long categoryId;

	@Column
	private long amount;

	@Column
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecurringFrequency frequency;

	@Column
	private Integer dayOfMonth; // 1-31 or 99 for Last Day

	@Enumerated(EnumType.STRING)
	@Column
	private DayOfWeek dayOfWeek;

	@Column
	private byte executionTimeHour; // 0-23

	@Column
	private byte executionTimeMinute; // 0-59

	@Column
	private long nextTriggerEpoch;

}

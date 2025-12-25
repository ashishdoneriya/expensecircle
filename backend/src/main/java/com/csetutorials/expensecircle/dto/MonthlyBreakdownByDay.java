package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.projection.DayOfMonthAmountProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBreakdownByDay {

	private String groupId;

	private short year;

	private byte month;

	private long totalAmount;

	private List<DayOfMonthAmountProjection> expenses;

}

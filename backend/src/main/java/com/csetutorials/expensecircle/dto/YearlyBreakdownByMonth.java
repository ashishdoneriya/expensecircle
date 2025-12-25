package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.projection.MonthAmountProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlyBreakdownByMonth {

	private String groupId;

	private short year;

	private long totalAmount;

	private List<MonthAmountProjection> expenses;

}

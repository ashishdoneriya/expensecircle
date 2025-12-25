package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.projection.CategoryAmountProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlyBreakdownByCategory {

	private String groupId;

	private short year;

	private long totalAmount;

	private List<CategoryAmountProjection> expenses;

}

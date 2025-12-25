package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.projection.TagAmountProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlyBreakdownByTag {

	private String groupId;

	private short year;

	private long totalAmount;

	private List<TagAmountProjection> expenses;

}

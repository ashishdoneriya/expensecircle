package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.projection.CategoryAmountProjection;
import com.csetutorials.expensecircle.projection.DayOfMonthAmountProjection;
import com.csetutorials.expensecircle.projection.MonthAmountProjection;
import com.csetutorials.expensecircle.projection.TagAmountProjection;
import com.csetutorials.expensecircle.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/stats")
@GroupMemberOnly
public class StatsController {

	@Autowired
	private StatsService service;

	@GetMapping("/yearly-total")
	public long getYearlyTotal(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year) {
		return service.getYearlyTotalAmount(groupId, year);
	}

	@GetMapping("/monthly-total")
	public long getMonthlyTotal(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month,
		@RequestParam(name = "categoryId", required = false, defaultValue = "") String categoryId) {
		if (categoryId.trim().isEmpty()) {
			return service.getMonthlyTotalAmount(groupId, year, month);
		} else {
			return service.getMonthlyTotalByCategory(groupId, year, month, categoryId);
		}
	}

	@GetMapping("/daily-total")
	public long getDailyTotal(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month,
		@RequestParam("dayOfMonth") byte day,
		@RequestParam(name = "categoryId", required = false, defaultValue = "") String categoryId) {
		if (categoryId.trim().isEmpty()) {
			return service.getDailyTotalAmount(groupId, year, month, day);
		} else {
			return service.getDailyTotalAmount(groupId, categoryId, year, month, day);
		}
	}

	@GetMapping("/yearly-breakdown-by-month")
	public List<MonthAmountProjection> getYearlyBreakdownByMonth(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year) {
		return service.getYearlyBreakdownByMonth(groupId, year);
	}

	@GetMapping("/yearly-breakdown-by-category")
	public List<CategoryAmountProjection> getYearlyBreakdownByCategory(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year) {
		return service.getYearlyBreakdownByCategory(groupId, year);
	}

	@GetMapping("/yearly-breakdown-by-tag")
	public List<TagAmountProjection> getYearlyBreakdownByTag(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year) {
		return service.getYearlyBreakdownByTag(groupId, year);
	}

	@GetMapping("/monthly-breakdown-by-day")
	public List<DayOfMonthAmountProjection> getMonthlyBreakdownByDay(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month,
		@RequestParam(name = "categoryId", required = false, defaultValue = "") String categoryId) {
		if (categoryId.trim().isEmpty()) {
			return service.getMonthlyBreakdownByDay(groupId, year, month);
		} else {
			return service.getMonthlyBreakdownByDay(groupId, categoryId, year, month);
		}
	}

	@GetMapping("/monthly-breakdown-by-category")
	public List<CategoryAmountProjection> getMonthlyBreakdownByCategory(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month) {
		return service.getMonthlyBreakdownByCategory(groupId, year, month);
	}

	@GetMapping("/monthly-breakdown-by-tag")
	public List<TagAmountProjection> getMonthlyBreakdownByTag(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month) {
		return service.getMonthlyBreakdownByTag(groupId, year, month);
	}

}

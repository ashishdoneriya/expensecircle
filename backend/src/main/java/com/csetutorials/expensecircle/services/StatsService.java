package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.projection.CategoryAmountProjection;
import com.csetutorials.expensecircle.projection.DayOfMonthAmountProjection;
import com.csetutorials.expensecircle.projection.MonthAmountProjection;
import com.csetutorials.expensecircle.projection.TagAmountProjection;
import com.csetutorials.expensecircle.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

	@Autowired
	private ExpenseRepository repo;


	public List<MonthAmountProjection> getYearlyBreakdownByMonth(String groupId, short year) {
		return repo.getYearlyBreakdownByMonth(groupId, year);
	}

	public List<CategoryAmountProjection> getYearlyBreakdownByCategory(String groupId, short year) {
		return repo.getYearlyBreakdownByCategory(groupId, year);
	}

	public List<TagAmountProjection> getYearlyBreakdownByTag(String groupId, short year) {
		return repo.getYearlyBreakdownByTag(groupId, year);
	}

	public List<DayOfMonthAmountProjection> getMonthlyBreakdownByDay(String groupId, short year, byte month) {
		return repo.getMonthlyBreakdownByDay(groupId, year, month);
	}

	public List<DayOfMonthAmountProjection> getMonthlyBreakdownByDay(String groupId, String categoryId, short year, byte month) {
		return repo.getMonthlyBreakdownByDayFilterByCategoryId(groupId, categoryId, year, month);
	}

	public List<CategoryAmountProjection> getMonthlyBreakdownByCategory(String groupId, short year, byte month) {
		return repo.getMonthlyBreakdownByCategory(groupId, year, month);
	}

	public List<TagAmountProjection> getMonthlyBreakdownByTag(String groupId, short year, byte month) {
		return repo.getMonthlyBreakdownByTag(groupId, year, month);
	}

	public long getYearlyTotalAmount(String groupId, short year) {
		return repo
			.getYearlyTotalAmount(groupId, year)
			.orElse(0L);
	}


	public long getYearlyTotalByCategory(String groupId, short year, String categoryId) {
		return repo
			.getYearlyTotalAmountFilterByCategoryId(groupId, year, categoryId)
			.orElse(0L);
	}

	public long getMonthlyTotalAmount(String groupId, short year, byte month) {
		return repo
			.getMonthlyTotalAmount(groupId, year, month)
			.orElse(0L);
	}

	public long getMonthlyTotalByCategory(String groupId, short year, byte month, String categoryId) {
		return repo
			.getMonthlyTotalAmountFilterByCategoryId(groupId, year, month, categoryId)
			.orElse(0L);
	}

	public long getDailyTotalAmount(String groupId, short year, byte month, byte dayOfMonth) {
		return repo
			.getDailyTotalAmount(groupId, year, month, dayOfMonth)
			.orElse(0L);
	}

	public long getDailyTotalAmount(String groupId, String categoryId, short year, byte month, byte dayOfMonth) {
		return repo
			.getDailyTotalAmountFilterByCategoryId(groupId, year, month, dayOfMonth, categoryId)
			.orElse(0L);
	}


}

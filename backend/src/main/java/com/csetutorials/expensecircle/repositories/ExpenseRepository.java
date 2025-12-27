package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.entities.ids.ExpenseIdWrapper;
import com.csetutorials.expensecircle.projection.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, ExpenseIdWrapper> {

	Optional<Expense> findByGroupIdAndExpenseId(
		String groupId, String expenseId);

	List<ExpenseProjection> findByGroupIdAndYearAndMonthAndDayOfMonth(
		String groupId, short year, byte month, byte dayOfMonth);

	List<ExpenseProjection> findByGroupIdAndCategoryIdAndYearAndMonthAndDayOfMonth(
		String groupId, String categoryId, short year, byte month, byte dayOfMonth);

	@Query("select e.month as month, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"group by e.month")
	List<MonthAmountProjection> getYearlyBreakdownByMonth(
		@Param("groupId") String groupId, @Param("year") short year);

	@Query("select e.categoryId as categoryId, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"group by e.categoryId")
	List<CategoryAmountProjection> getYearlyBreakdownByCategory(
		@Param("groupId") String groupId, @Param("year") short year);

	@Query("select e.dayOfMonth as dayOfMonth, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"and e.month = :month group by e.dayOfMonth order by e.dayOfMonth")
	List<DayOfMonthAmountProjection> getMonthlyBreakdownByDay(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select e.dayOfMonth as dayOfMonth, sum(e.amount) as amount from Expense e " +
		"where e.groupId = :groupId and e.year = :year " +
		"and e.month = :month and e.categoryId = :categoryId group by e.dayOfMonth order by e.dayOfMonth")
	List<DayOfMonthAmountProjection> getMonthlyBreakdownByDayFilterByCategoryId(
		@Param("groupId") String groupId, @Param("categoryId") String categoryId,
		@Param("year") short year, @Param("month") byte month);

	@Query("select e.categoryId as categoryId, sum(e.amount) as amount from Expense e where e.groupId = :groupId " +
		"and e.year = :year and e.month = :month group by e.categoryId")
	List<CategoryAmountProjection> getMonthlyBreakdownByCategory(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year")
	Optional<Long> getYearlyTotalAmount(
		@Param("groupId") String groupId, @Param("year") short year);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.categoryId = :categoryId")
	Optional<Long> getYearlyTotalAmountFilterByCategoryId(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("categoryId") String categoryId);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month")
	Optional<Long> getMonthlyTotalAmount(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.categoryId = :categoryId")
	Optional<Long> getMonthlyTotalAmountFilterByCategoryId(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month, @Param("categoryId") String categoryId);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.dayOfMonth = :dayOfMonth")
	Optional<Long> getDailyTotalAmount(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month, @Param("dayOfMonth") byte dayOfMonth);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.dayOfMonth = :dayOfMonth and e.categoryId = :categoryId")
	Optional<Long> getDailyTotalAmountFilterByCategoryId(
		@Param("groupId") String groupId, @Param("year") short year,
		@Param("month") byte month, @Param("dayOfMonth") byte dayOfMonth,
		@Param("categoryId") String categoryId);

	@Query("SELECT et.tagId as tagId, SUM(e.amount) as amount FROM Expense e " +
		"JOIN ExpenseTag et on et.groupId = e.groupId and et.expenseId = e.expenseId " +
		"and e.groupId = :groupId and e.groupId = :groupId and e.year = :year group by tagId")
	List<TagAmountProjection> getYearlyBreakdownByTag(
		@Param("groupId") String groupId,
		@Param("year") short year);

	@Query("SELECT et.tagId as tagId, SUM(e.amount) as amount FROM Expense e " +
		"JOIN ExpenseTag et on et.groupId = e.groupId and et.expenseId = e.expenseId " +
		"and e.groupId = :groupId and e.groupId = :groupId and e.year = :year and e.month = :month group by tagId")
	List<TagAmountProjection> getMonthlyBreakdownByTag(
		@Param("groupId") String groupId,
		@Param("year") short year,
		@Param("month") byte month);

	@Transactional
	@Modifying
	@Query("update Expense e set e.categoryId = NULL where e.groupId = :groupId and e.categoryId = :categoryId")
	void deleteByCategoryId(String groupId, String categoryId);

	@Transactional
	@Modifying
	@Query("delete from Expense e where e.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") String groupId);

	@Transactional
	@Modifying
	@Query("delete from Expense e where e.groupId = :groupId and e.expenseId = :expenseId")
    void deleteByGroupIdAndExpenseId(@Param("groupId") String groupId, @Param("expenseId") String expenseId);
}

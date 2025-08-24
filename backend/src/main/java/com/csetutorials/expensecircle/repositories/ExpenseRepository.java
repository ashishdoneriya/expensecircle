package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.projection.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Expense> {

	Optional<Expense> findByGroupIdAndExpenseId(
		long groupId, long expenseId);

	List<ExpenseProjection> findByGroupIdAndYearAndMonthAndDayOfMonth(
		long groupId, short year, byte month, byte dayOfMonth);

	List<ExpenseProjection> findByGroupIdAndCategoryIdAndYearAndMonthAndDayOfMonth(
		long groupId, long categoryId, short year, byte month, byte dayOfMonth);

	@Modifying
	@Transactional
	@Query("delete from Expense e where e.groupId = :groupId and e.expenseId = :expenseId")
	void deleteExpense(@Param("groupId") long groupId, @Param("expenseId") long expenseId);

	@Modifying
	@Transactional
	@Query("delete from Expense e where e.groupId = :groupId")
	void deleteExpenses(@Param("groupId") long groupId);

	@Query("select e.month as month, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"group by e.month")
	List<MonthAmountProjection> getYearlyBreakdownByMonth(
		@Param("groupId") long groupId, @Param("year") short year);

	@Query("select e.categoryId as categoryId, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"group by e.categoryId")
	List<CategoryAmountProjection> getYearlyBreakdownByCategory(
		@Param("groupId") long groupId, @Param("year") short year);

	@Query("select e.dayOfMonth as dayOfMonth, sum(e.amount) as amount from Expense e where e.groupId = :groupId and e.year = :year " +
		"and e.month = :month group by e.dayOfMonth order by e.dayOfMonth")
	List<DayOfMonthAmountProjection> getMonthlyBreakdownByDay(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select e.dayOfMonth as dayOfMonth, sum(e.amount) as amount from Expense e " +
		"where e.groupId = :groupId and e.year = :year " +
		"and e.month = :month and e.categoryId = :categoryId group by e.dayOfMonth order by e.dayOfMonth")
	List<DayOfMonthAmountProjection> getMonthlyBreakdownByDayFilterByCategoryId(
		@Param("groupId") long groupId, @Param("categoryId") long categoryId,
		@Param("year") short year, @Param("month") byte month);

	@Query("select e.categoryId as categoryId, sum(e.amount) as amount from Expense e where e.groupId = :groupId " +
		"and e.year = :year and e.month = :month group by e.categoryId")
	List<CategoryAmountProjection> getMonthlyBreakdownByCategory(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year")
	Optional<Long> getYearlyTotalAmount(
		@Param("groupId") long groupId, @Param("year") short year);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.categoryId = :categoryId")
	Optional<Long> getYearlyTotalAmountFilterByCategoryId(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("categoryId") long categoryId);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month")
	Optional<Long> getMonthlyTotalAmount(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.categoryId = :categoryId")
	Optional<Long> getMonthlyTotalAmountFilterByCategoryId(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month, @Param("categoryId") long categoryId);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.dayOfMonth = :dayOfMonth")
	Optional<Long> getDailyTotalAmount(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month, @Param("dayOfMonth") byte dayOfMonth);

	@Query("select sum(e.amount) from Expense e where e.groupId = :groupId and e.year = :year" +
		" and e.month = :month and e.dayOfMonth = :dayOfMonth and e.categoryId = :categoryId")
	Optional<Long> getDailyTotalAmountFilterByCategoryId(
		@Param("groupId") long groupId, @Param("year") short year,
		@Param("month") byte month, @Param("dayOfMonth") byte dayOfMonth,
		@Param("categoryId") long categoryId);

	@Query(value = "SELECT tag AS tagId, SUM(e.amount) AS amount " +
		"FROM expenses e, JSON_TABLE(e.tags, '$[*]' COLUMNS (tag BIGINT PATH '$')) AS jt " +
		"WHERE e.groupId = :groupId " +
		"AND e.year = :year " +
		"GROUP BY tag",
		nativeQuery = true)
	List<TagAmountProjection> getYearlyBreakdownByTag(
		@Param("groupId") long groupId,
		@Param("year") short year);

	@Query(value = "SELECT tag AS tagId, SUM(e.amount) AS amount " +
		"FROM expenses e, JSON_TABLE(e.tags, '$[*]' COLUMNS (tag BIGINT PATH '$')) AS jt " +
		"WHERE e.groupId = :groupId " +
		"AND e.year = :year " +
		"AND e.month = :month " +
		"GROUP BY tag",
		nativeQuery = true)
	List<TagAmountProjection> getMonthlyBreakdownByTag(
		@Param("groupId") long groupId,
		@Param("year") short year,
		@Param("month") byte month);

	@Transactional
	@Modifying
	@Query("update Expense e set e.categoryId = 0 where e.groupId = :groupId and e.categoryId = :categoryId")
	void deleteCategory(long groupId, long categoryId);

	@Transactional
	@Modifying
	@Query(value = "update expenses SET tags = JSON_REMOVE(tags, JSON_UNQUOTE(JSON_SEARCH(tags, 'all', :tagId)))" +
		" where groupId = :groupId and JSON_CONTAINS(tags, '[:tagId]')", nativeQuery = true)
	void deleteTag(long groupId, long tagId);
}

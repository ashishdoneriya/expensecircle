package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.entities.ids.RecurringExpenseId;
import com.csetutorials.expensecircle.projection.RecurringExpenseSummaryProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, RecurringExpenseId> {

	List<RecurringExpenseSummaryProjection> findByGroupId(@Param("groupId") String groupId);

	Optional<RecurringExpense> findByGroupIdAndRecurringId(
		@Param("groupId") String groupId, @Param("recurringId") String recurringId);

	@Modifying
	@Transactional
	@Query("delete from RecurringExpense re where re.groupId = :groupId and re.recurringId = :recurringId")
	void deleteByGroupIdAndRecurringId(@Param("groupId") String groupId, @Param("recurringId") String recurringId);

	@Modifying
	@Transactional
	@Query("delete from RecurringExpense re where re.groupId = :groupId")
	void deleteByGroupId(@Param("groupId") String groupId);

	@Modifying
	@Transactional
	@Query("update RecurringExpense re set re.nextTriggerEpoch = :nextTriggerEpoch where groupId = :groupId and re.recurringId = :recurringId")
	void updateNextTriggerEpoch(
		@Param("groupId") String groupId,
		@Param("recurringId") String recurringId,
		@Param("nextTriggerEpoch") long nextTriggerEpoch);

	@Query("select re from RecurringExpense re where re.nextTriggerEpoch < :time")
	List<RecurringExpense> findDue(@Param("time") long time, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update RecurringExpense e set e.categoryId = NULL where e.groupId = :groupId and e.categoryId = :categoryId")
	void deleteByCategoryId(String groupId, String categoryId);

}

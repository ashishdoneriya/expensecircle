package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.entities.ids.RecurringExpenseId;
import com.csetutorials.expensecircle.projection.RecurringExpenseSummaryProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, RecurringExpenseId> {

	List<RecurringExpenseSummaryProjection> findByGroupId(@Param("groupId") long groupId);

	Optional<RecurringExpense> findByGroupIdAndRecurringId(
		@Param("groupId") long groupId, @Param("recurringId") long recurringId);

	@Query("delete from RecurringExpense re where re.groupId = :groupId and re.recurringId = :recurringId")
	void deleteRecord(@Param("groupId") long groupId, @Param("recurringId") long recurringId);

	@Query("update RecurringExpense re set re.nextTriggerEpoch = :nextTriggerEpoch where groupId = :groupId and re.recurringId = :recurringId and re.nextTriggerEpoch = :nextTriggerEpoch")
	void updateNextTriggerEpoch(
		@Param("groupId") long groupId,
		@Param("recurringId") long recurringId,
		@Param("nextTriggerEpoch") long nextTriggerEpoch);

	@Query("select re from RecurringExpense re where re.nextTriggerEpoch < :time")
	List<RecurringExpense> findDue(@Param("time") long time, Pageable pageable);
}

package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.RecurringExpenseTag;
import com.csetutorials.expensecircle.entities.ids.RecurringExpenseTagId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecurringExpenseTagRepository extends JpaRepository<RecurringExpenseTag, RecurringExpenseTagId> {

	List<RecurringExpenseTag> findByGroupIdAndRecurringId(
		@Param("groupId") String groupId, @Param("recurringId") String recurringId);

	@Modifying
	@Transactional
	@Query("delete from RecurringExpenseTag et where et.groupId = :groupId and et.tagId = :tagId")
    void deleteByGroupIdAndTagId(@Param("groupId") String groupId, @Param("tagId") String tagId);

	@Modifying
	@Transactional
	@Query("delete from RecurringExpenseTag et where et.groupId = :groupId and et.recurringId = :recurringId")
    void deleteByGroupIdAndRecurringId(
		@Param("groupId") String groupId, @Param("recurringId") String recurringId);

	@Modifying
	@Transactional
	@Query("delete from RecurringExpenseTag et where et.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") String groupId);

}

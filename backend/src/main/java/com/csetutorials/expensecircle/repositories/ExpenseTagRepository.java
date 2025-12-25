package com.csetutorials.expensecircle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csetutorials.expensecircle.entities.ExpenseTag;
import com.csetutorials.expensecircle.entities.ids.ExpenseTagId;

import jakarta.transaction.Transactional;

public interface ExpenseTagRepository extends JpaRepository<ExpenseTag, ExpenseTagId> {

	List<ExpenseTag> findByGroupIdAndExpenseId(
		@Param("groupId") String groupId, @Param("expenseId") String expenseId);

	@Modifying
	@Transactional
	@Query("delete from ExpenseTag et where et.groupId = :groupId and et.tagId = :tagId")
    void deleteByGroupIdAndTagId(@Param("groupId") String groupId, @Param("tagId") String tagId);

	@Modifying
	@Transactional
	@Query("delete from ExpenseTag et where et.groupId = :groupId and et.expenseId = :expenseId")
    void deleteByGroupIdAndExpenseId(@Param("groupId") String groupId, @Param("expenseId") String expenseId);

	@Modifying
	@Transactional
	@Query("delete from ExpenseTag et where et.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") String groupId);

}

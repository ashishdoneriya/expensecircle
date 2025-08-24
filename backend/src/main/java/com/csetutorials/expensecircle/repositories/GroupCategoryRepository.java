package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.GroupCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, GroupCategory> {

	List<GroupCategory> findAllByGroupId(long groupId);

	@Modifying
	@Transactional
	@Query("delete from GroupCategory gc where gc.groupId = :groupId and gc.categoryId = :categoryId")
	void deleteCategory(long groupId, long categoryId);

	@Modifying
	@Transactional
	@Query("update GroupCategory gc set gc.categoryName = :newCategoryName where gc.groupId = :groupId and gc.categoryId = :categoryId")
	void renameCategory(long groupId, long categoryId, String newCategoryName);
}

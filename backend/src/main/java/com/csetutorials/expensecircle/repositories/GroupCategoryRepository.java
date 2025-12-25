package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.GroupCategory;
import com.csetutorials.expensecircle.entities.ids.GroupCategoryId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, GroupCategoryId> {

	List<GroupCategory> findAllByGroupId(String groupId);

	@Modifying
	@Transactional
	@Query("delete from GroupCategory gc where gc.groupId = :groupId and gc.categoryId = :categoryId")
	void deleteByCategoryId(String groupId, String categoryId);

	@Modifying
	@Transactional
	@Query("update GroupCategory gc set gc.categoryName = :newCategoryName where gc.groupId = :groupId and gc.categoryId = :categoryId")
	void renameCategory(String groupId, String categoryId, String newCategoryName);

	@Modifying
	@Transactional
	@Query("delete from GroupCategory gc where gc.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") String groupId);
}

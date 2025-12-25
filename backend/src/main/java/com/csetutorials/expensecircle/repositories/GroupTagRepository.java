package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.GroupTag;
import com.csetutorials.expensecircle.entities.ids.GroupTagId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupTagRepository extends JpaRepository<GroupTag, GroupTagId> {

	List<GroupTag> findAllByGroupId(String groupId);

	@Modifying
	@Transactional
	@Query("delete from GroupTag gc where gc.groupId = :groupId and gc.tagId = :tagId")
	void deleteByGroupIdAndTagId(String groupId, String tagId);

	@Modifying
	@Transactional
	@Query("update GroupTag gc set gc.tagName = :tagName where gc.groupId = :groupId and gc.tagId = :tagId")
	void renameTag(String groupId, String tagId, String tagName);

	@Modifying
	@Transactional
	@Query("delete from GroupTag gc where gc.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") String groupId);
}

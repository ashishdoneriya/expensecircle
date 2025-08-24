package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.GroupTag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupTagRepository extends JpaRepository<GroupTag, GroupTag> {

	List<GroupTag> findAllByGroupId(long groupId);

	@Modifying
	@Transactional
	@Query("delete from GroupTag gc where gc.groupId = :groupId and gc.tagId = :tagId")
	void deleteTag(long groupId, long tagId);

	@Modifying
	@Transactional
	@Query("update GroupTag gc set gc.tagName = :tagName where gc.groupId = :groupId and gc.tagId = :tagId")
	void renameTag(long groupId, long tagId, String tagName);
}

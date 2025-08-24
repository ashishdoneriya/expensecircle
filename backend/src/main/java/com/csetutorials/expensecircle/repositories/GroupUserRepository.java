package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.entities.GroupUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUser> {


	@Modifying
	@Transactional
	@Query("delete from GroupUser gu where gu.groupId = :groupId and gu.userId = :userId")
	void deleteByGroupIdAndUserId(@Param("groupId") long groupId, @Param("userId") String userId);

	Optional<GroupUser> findByGroupIdAndUserId(@Param("groupId") long groupId, @Param("userId") String userId);

	boolean existsByGroupIdAndUserId(long groupId, String userId);

	List<GroupUser> findByUserId(@Param("userId") String userId);

	List<GroupUser> findByGroupId(@Param("groupId") long groupId);

	@Modifying
	@Transactional
	@Query("update GroupUser gu set gu.groupName = :groupName where gu.groupId = :groupId")
	void updateGroupName(@Param("groupId") long groupId, String groupName);

	@Modifying
	@Transactional
	@Query("update GroupUser gu set gu.userName = :userName where gu.userId = :userId")
	void updateUserName(@Param("userId") long userId, String userName);

	@Modifying
	@Transactional
	@Query("delete from GroupUser gu where gu.groupId = :groupId")
	void deleteByGroupId(@Param("groupId") long groupId);

	@Modifying
	@Transactional
	@Query("update GroupUser gu set gu.role = :role where gu.groupId = :groupId and gu.userId = :userId")
	void changeUserPermission(long groupId, String userId, Role role);

	@Modifying
	@Transactional
	@Query("update GroupUser gu set gu.role = 'ADMIN' where gu.groupId = :groupId")
	void makeAllAdmin(long groupId);
}

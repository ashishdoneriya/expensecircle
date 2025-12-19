package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.entities.GroupUserMembership;
import com.csetutorials.expensecircle.entities.ids.GroupUserId;
import com.csetutorials.expensecircle.projection.GroupUserProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupUserMembershipRepository extends JpaRepository<GroupUserMembership, GroupUserId> {

	@Query("select u.userId as userId, u.name as userName, gu.role as role from User u JOIN GroupUserMembership gu on u.userId = gu.userId and gu.groupId = :groupId")
	List<GroupUserProjection> findByGroupId(@Param("groupId") Long groupId);

	@Query("select g.groupId as groupId, g.groupName as groupName, gu.role as role from Group g JOIN GroupUserMembership gu on g.groupId = gu.groupId and gu.userId = :userId")
	List<GroupUserProjection> findByUserId(@Param("userId") String userId);

	@Query("select gu from GroupUserMembership gu where gu.groupId = :groupId and gu.userId = :userId")
	Optional<GroupUserMembership> findByGroupIdAndUserId(
		@Param("groupId") Long groupId, @Param("userId") String userId);

	@Modifying
	@Transactional
	@Query("UPDATE GroupUserMembership gu SET gu.role = 'ADMIN' WHERE gu.id.groupId = :groupId")
	void makeAllMembersAdmin(@Param("groupId") Long groupId);

	@Modifying
	@Transactional
	@Query("UPDATE GroupUserMembership gu SET gu.role = :newRole WHERE gu.id.userId = :userId AND gu.id.groupId = :groupId")
	void updateMemberRole(@Param("groupId") Long groupId,
						 @Param("userId") String userId,
						 @Param("newRole") Role role);

	@Modifying
	@Transactional
	@Query("DELETE from GroupUserMembership gu WHERE gu.id.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") long groupId);

}

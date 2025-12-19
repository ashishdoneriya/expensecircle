package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.dto.GroupUserResponseDto;
import com.csetutorials.expensecircle.entities.GroupUserMembership;
import com.csetutorials.expensecircle.entities.ids.GroupUserId;
import com.csetutorials.expensecircle.projection.GroupUserProjection;
import com.csetutorials.expensecircle.repositories.GroupUserMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupUserMembershipService {

	@Autowired
	private GroupUserMembershipRepository repo;
	@Autowired
	private AsyncCalls asyncCalls;

	public List<GroupUserResponseDto> findByGroupId(Long groupId) {
		return convert(repo.findByGroupId(groupId));
	}


	public List<GroupUserResponseDto> findByUserId(String userId) {
		return convert(repo.findByUserId(userId));
	}


	public void addMember(Long groupId, String userId, Role role) {

		GroupUserMembership membership = new GroupUserMembership();
		membership.setUserId(userId);
		membership.setGroupId(groupId);
		membership.setRole(role);
		repo.save(membership);

	}

	public void remove(Long groupId, String userId) {
		repo.deleteById(new GroupUserId(groupId, userId));
	}

	public void exitGroup(long groupId, String userId) {
		Optional<GroupUserMembership> opt = repo.findByGroupIdAndUserId(groupId, userId);
		if (opt.isEmpty()) {
			return;
		}
		remove(groupId, userId);
		Role role = opt.get().getRole();
		if (Role.MEMBER == role) {
			return;
		}
		List<GroupUserProjection> list = repo.findByGroupId(groupId);
		if (list.isEmpty()) {
			asyncCalls.deleteGroup(groupId);
			return;
		}
		long numAdmins = list.stream().filter(obj -> obj.getRole() == Role.ADMIN).count();
		if (numAdmins == 0) {
			repo.makeAllMembersAdmin(groupId);
		}
	}

	public void updateMemberRole(long groupId, String userId, Role role) {
		repo.updateMemberRole(groupId, userId, role);
	}

	private List<GroupUserResponseDto> convert(List<GroupUserProjection> list) {
		return list.stream()
			.map(obj ->
				new GroupUserResponseDto(
					obj.getUserId(),
					obj.getUserName(),
					obj.getGroupId(),
					obj.getGroupName(),
					obj.getRole()))
			.toList();
	}

}

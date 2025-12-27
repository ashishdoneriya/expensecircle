package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.dto.GroupUserResponseDto;
import com.csetutorials.expensecircle.entities.GroupUserMembership;
import com.csetutorials.expensecircle.entities.ids.GroupUserId;
import com.csetutorials.expensecircle.projection.GroupUserProjection;
import com.csetutorials.expensecircle.repositories.GroupUserMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupUserMembershipService {

	@Autowired
	private GroupUserMembershipRepository repo;
	@Autowired
	private AsyncCalls asyncCalls;

	public Optional<GroupUserResponseDto> findByGroupIdAndUserId(String groupId, String userId) {
		return repo.findByGroupIdAndUserId(groupId, userId).map(this::convert);
	}

	public List<GroupUserResponseDto> findByGroupId(String groupId) {
		return convert(repo.findByGroupId(groupId));
	}


	public List<GroupUserResponseDto> findByUserId(String userId) {
		return convert(repo.findByUserId(userId));
	}


	public void addMember(String groupId, String email, Role role) {

		GroupUserMembership membership = new GroupUserMembership();
		membership.setUserId(email);
		membership.setGroupId(groupId);
		membership.setRole(role);
		repo.save(membership);

	}

	@Async
	public void sendInvite(String groupId, String email, Role role, String inviterUserId) {
		GroupUserMembership membership = new GroupUserMembership();
		membership.setUserId(email);
		membership.setGroupId(groupId);
		membership.setRole(role);
		repo.save(membership);

	}

	public void remove(String groupId, String userId) {
		repo.deleteById(new GroupUserId(groupId, userId));
	}

	public void exitGroup(String groupId, String userId) {
		Optional<Role> opt = repo.findRoleByGroupIdAndUserId(groupId, userId);
		if (opt.isEmpty()) {
			return;
		}
		remove(groupId, userId);
		Role role = opt.get();
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

	public void updateMemberRole(String groupId, String userId, Role role) {
		repo.updateMemberRole(groupId, userId, role);
	}

	private List<GroupUserResponseDto> convert(List<GroupUserProjection> list) {
		return list.stream()
			.map(this::convert)
			.toList();
	}

	private GroupUserResponseDto convert(
		GroupUserProjection obj) {
		return new GroupUserResponseDto(
			obj.getUserId(),
			obj.getUserName(),
			obj.getGroupId(),
			obj.getGroupName(),
			obj.getRole());
	}


}

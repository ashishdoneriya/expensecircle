package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupAdminOnly;
import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.beans.*;
import com.csetutorials.expensecircle.dto.*;
import com.csetutorials.expensecircle.entities.Group;
import com.csetutorials.expensecircle.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupsController {

	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupUserMembershipService groupUserMembershipService;
	@Autowired
	private AsyncCalls asyncCalls;

	/**
	 * This method returns list of groups of the current logged in user
	 */
	@GetMapping("/me")
	public List<GroupUserResponseDto> seeMyGroups() {
		return groupUserMembershipService.findByUserId(loggedInUserInfoService.getId());
	}

	@GetMapping("/{groupId}")
	@GroupMemberOnly
	public GroupUserResponseDto getSpecificGroupInfo(@PathVariable("groupId") String groupId) {
		return groupUserMembershipService
			.findByGroupIdAndUserId(groupId, loggedInUserInfoService.getId())
			.orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Group not found"));
	}

	@GetMapping("/{groupId}/members")
	@GroupMemberOnly
	public List<GroupUserResponseDto> listGroupMembers(@PathVariable("groupId") String groupId) {
		return groupUserMembershipService.findByGroupId(groupId);
	}

	@PostMapping
	public void createGroup(@RequestBody Name name) {
		Group group = groupService.create(name.getName());
		groupUserMembershipService.addMember(
			group.getGroupId(), loggedInUserInfoService.getId(), Role.ADMIN);
	}

	@PostMapping("/{groupId}")
	@GroupAdminOnly
	public void changeGroupName(
		@PathVariable("groupId") String groupId,
		@RequestBody @Valid Name name) {
		groupService.rename(groupId, name.getName());
	}

	@DeleteMapping("/{groupId}")
	@GroupAdminOnly
	public void deleteGroup(@PathVariable("groupId") String groupId) {
		asyncCalls.deleteGroup(groupId);
	}

	@PostMapping("/{groupId}/exit")
	@GroupMemberOnly
	public void exitFromGroup(@PathVariable("groupId") String groupId) {
		groupUserMembershipService.exitGroup(groupId, loggedInUserInfoService.getId());
	}

	@DeleteMapping("/{groupId}/remove-user")
	@GroupAdminOnly
	public void removeUser(
		@PathVariable("groupId") String groupId,
		@RequestBody @Valid UserIdWrapper userId) {
		groupUserMembershipService.remove(groupId, userId.getUserId());
	}

	@PostMapping("/{groupId}/add-user")
	@GroupAdminOnly
	public void addUser(
		@PathVariable("groupId") String groupId,
		@RequestBody @Valid EmaiIdWrapper wrapper) {
		groupUserMembershipService.addMember(groupId, wrapper.getEmail(), Role.MEMBER);
	}

	@PostMapping("/{groupId}/change-user-permissions")
	@GroupAdminOnly
	public void changeUserPermission(
		@PathVariable("groupId") String groupId,
		@RequestBody @Valid NewUserPermissionRequest request) {
		groupUserMembershipService.updateMemberRole(
			groupId, request.getUserId(), request.getRole());
	}


}

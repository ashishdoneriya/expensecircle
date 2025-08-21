package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.beans.*;
import com.csetutorials.expensecircle.entities.GroupUser;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.GroupUserService;
import com.csetutorials.expensecircle.services.LoggedInUserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupsController {

	@Autowired
	private GroupUserService groupUserService;
	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;

	@GetMapping
	public List<GroupUser> getGroups() {
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		List<GroupUser> list = groupUserService.findByUserId(userInfo.getEmail());
		list.stream()
			.filter(obj -> obj.getUserName() == null || obj.getUserName().trim().isEmpty())
			.forEach(obj -> {
				obj.setUserName(userInfo.getName());
				groupUserService.save(obj);
			});
		return list;
	}

	@GetMapping("/{groupId}")
	public GroupUser getGroupInfoForUser(@PathVariable("groupId") long groupId) {
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		Optional<GroupUser> opt = groupUserService.findByGroupIdAndUserId(groupId, userInfo.getEmail());
		if (opt.isPresent()) {
			GroupUser groupUser = opt.get();
			if (groupUser.getUserName() == null || groupUser.getUserName().trim().isEmpty()) {
				groupUser.setUserName(userInfo.getName());
				groupUserService.save(groupUser);
			}
			return groupUser;
		}
		return null;
	}

	@GetMapping("/{groupId}/users")
	public List<GroupUser> getUsersList(@PathVariable("groupId") long groupId) {
		return groupUserService.findByGroupId(groupId);
	}

	@PostMapping
	public void addGroup(@RequestBody Name name) {
		long groupId = System.currentTimeMillis();
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		groupUserService.addUser(groupId, userInfo.getEmail(), Role.ADMIN, name.getName(), userInfo.getName());
	}

	@PostMapping("/{groupId}")
	public void changeGroupName(@PathVariable("groupId") long groupId, @RequestBody @Valid UserId userId) {
		groupUserService.checkCurrentUserAdmin(groupId);
		groupUserService.updateGroupName(groupId, userId.getUserId());
	}

	@DeleteMapping("/{groupId}")
	public void deleteGroup(@PathVariable("groupId") long groupId) {
		groupUserService.checkCurrentUserAdmin(groupId);
		groupUserService.deleteByGroupId(groupId);
	}

	@PostMapping("/{groupId}/exit")
	public void exitFromGroup(@PathVariable("groupId") long groupId) {
		groupUserService.exitGroup(groupId);
	}

	@PostMapping("/{groupId}/remove-user")
	public void removeUser(@PathVariable("groupId") long groupId, @RequestBody @Valid UserId userId) {
		groupUserService.checkCurrentUserAdmin(groupId);
		groupUserService.deleteByGroupIdAndUserId(groupId, userId.getUserId());
		if (groupUserService.findByGroupId(groupId).isEmpty()) {
			expenseCoordinator.deleteExpenses(groupId);
		}
	}

	@PostMapping("/{groupId}/add-user")
	public void addUser(@PathVariable("groupId") long groupId, @RequestBody @Valid UserId userId) {
		groupUserService.checkCurrentUserAdmin(groupId);
		Optional<GroupUser> currentUser = groupUserService.findByGroupIdAndUserId(
			groupId, loggedInUserInfoService.getInfo().getEmail());
		groupUserService.addUser(
			groupId, userId.getUserId(), Role.MEMBER, currentUser.get().getGroupName(), "");
	}

	@PostMapping("/{groupId}/change-user-permissions")
	public void changeUserPermission(
		@PathVariable("groupId") long groupId,
		@RequestBody @Valid NewUserPermissionRequest request) {
		groupUserService.checkCurrentUserAdmin(groupId);
		groupUserService.changeUserPermission(groupId, request.getUserId(), request.getRole());
	}


}

package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.entities.GroupUser;
import com.csetutorials.expensecircle.exceptions.UnauthorizedAccessException;
import com.csetutorials.expensecircle.repositories.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupUserService {

	@Autowired
	private GroupUserRepository repo;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;

	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;

	public void addUser(long groupId, String userId, Role role, String groupName, String userName) {
		repo.save(new GroupUser(groupId, userId, role, groupName, userName));
	}

	public void changeUserPermission(long groupId, String userId, Role role) {

		repo.changeUserPermission(groupId, userId, role);
	}

	public void checkCurrentUserAdmin(long groupId) {
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		Optional<GroupUser> userGroup = repo.findByGroupIdAndUserId(groupId, userInfo.getEmail());
		if (userGroup.isEmpty() || userGroup.get().getRole() != Role.ADMIN) {
			throw new UnauthorizedAccessException();
		}
	}

	public List<GroupUser> findByUserId(String userId) {
		return repo.findByUserId(userId);
	}

	public Optional<GroupUser> findByGroupIdAndUserId(long groupId, String userId) {
		return repo.findByGroupIdAndUserId(groupId, userId);
	}

	public List<GroupUser> findByGroupId(long groupId) {
		return repo.findByGroupId(groupId);
	}

	public void updateGroupName(long groupId, String name) {
		repo.updateGroupName(groupId, name);
	}

	public void deleteByGroupId(long groupId) {
		repo.deleteByGroupId(groupId);
	}

	public void deleteByGroupIdAndUserId(long groupId, String userId) {
		repo.deleteByGroupIdAndUserId(groupId, userId);
	}

	public void save(GroupUser groupUser) {
		repo.save(groupUser);
	}

	public void exitGroup(long groupId) {
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		Optional<GroupUser> opt = repo.findByGroupIdAndUserId(groupId, userInfo.getEmail());
		if (opt.isEmpty()) {
			return;
		}
		GroupUser groupUser = opt.get();
		repo.deleteByGroupIdAndUserId(groupId, loggedInUserInfoService.getInfo().getEmail());
		if (groupUser.getRole() == Role.MEMBER) {
			return;
		}
		List<GroupUser> list = repo.findByGroupId(groupId);
		if (list.isEmpty()) {
			expenseCoordinator.deleteExpenses(groupId);
			return;
		}
		long numAdmins = list.stream().filter(obj -> obj.getRole() == Role.ADMIN).count();
		if (numAdmins == 0) {
			repo.makeAllAdmin(groupId);
		}

	}
}

package com.csetutorials.expensecircle.security;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.repositories.GroupUserMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("groupSecurityService")
public class GroupSecurityService {

	@Autowired
	private GroupUserMembershipRepository repo;

	public boolean isMember(String groupId) {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		UserInfo userInfo = (UserInfo) auth.getPrincipal();
		if (userInfo == null) {
			return false;
		}
		Optional<Role> opt = repo.findRoleByGroupIdAndUserId(groupId, userInfo.getUserId());
		return opt.isPresent();
	}

	public boolean isAdmin(String groupId) {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		UserInfo userInfo = (UserInfo) auth.getPrincipal();
		if (userInfo == null) {
			return false;
		}
		Optional<Role> opt = repo.findRoleByGroupIdAndUserId(groupId, userInfo.getUserId());
		return opt.isPresent() && opt.get() == Role.ADMIN;
	}

}

package com.csetutorials.expensecircle.security;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.entities.GroupUserMembership;
import com.csetutorials.expensecircle.repositories.GroupUserMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("groupSecurityService")
public class GroupSecurityService {

	@Autowired
	private GroupUserMembershipRepository repo;

	public boolean isMember(Long groupId) {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		UserInfo userInfo = (UserInfo) auth.getPrincipal();
		if (userInfo == null) {
			return false;
		}
		String email = userInfo.getEmail();
		Optional<GroupUserMembership> opt = repo.findByGroupIdAndUserId(groupId, email);
		return opt.isPresent();
	}

	public boolean isAdmin(Long groupId) {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		UserInfo userInfo = (UserInfo) auth.getPrincipal();
		if (userInfo == null) {
			return false;
		}
		String email = userInfo.getEmail();
		Optional<GroupUserMembership> opt = repo.findByGroupIdAndUserId(groupId, email);
		return opt.isPresent() && opt.get().getRole() == Role.ADMIN;
	}

}

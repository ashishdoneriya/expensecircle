package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.beans.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupInfoResponse {

	private long groupId;
	private String userId;
	private Role role;
	private String userName;
	private String groupName;


}

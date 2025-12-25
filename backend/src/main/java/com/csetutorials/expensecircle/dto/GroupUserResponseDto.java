package com.csetutorials.expensecircle.dto;

import com.csetutorials.expensecircle.beans.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupUserResponseDto {
	private String userId;
	private String userName;
	private String groupId;
	private String groupName;
	private Role role;

	public GroupUserResponseDto(String userId, String userName, String role) {
		this.userId = userId;
		this.userName = userName;
		this.role = Role.valueOf(role);
	}

}
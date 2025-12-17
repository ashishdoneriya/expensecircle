package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.beans.Role;
import com.csetutorials.expensecircle.entities.ids.GroupUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groupUsers")
@IdClass(GroupUserId.class)
public class GroupUser {

	@Id
	private long groupId;

	@Id
	private String userId;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 200)
	private String groupName;

	@Column(length = 254)
	private String userName;

}

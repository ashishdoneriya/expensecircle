package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.beans.InviteStatus;
import com.csetutorials.expensecircle.entities.ids.GroupInviteId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groupInvites")
@IdClass(GroupInviteId.class)
public class GroupInvite {

	@Id
	String groupId;

	@Id
	String inviteId;

	@Column
	String invitedUserId;

	@Column(length = 255)
	String invitedEmail;

	@Column(nullable = false)
	String inviterUserId;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	InviteStatus status;

	@Column(nullable = false, updatable = false)
	private long createdAt;

	@Column(nullable = false)
	private long expiresAt;

	@Column(nullable = false)
	private long consumedAt;

}

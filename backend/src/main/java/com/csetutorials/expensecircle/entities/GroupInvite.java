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
	Long groupId;
	@Id
	Long inviteId;
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
	private Instant createdAt;
	@Column(nullable = false)
	private Instant expiresAt;
	@Column(nullable = false)
	private Instant consumedAt;

	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
	}

	public boolean isEmailInvite() {
		return invitedEmail != null;
	}

	public boolean isUserInvite() {
		return invitedUserId != null;
	}

	public void validateTarget() {
		if ((invitedUserId == null && invitedEmail == null)
			|| (invitedUserId != null && invitedEmail != null)) {
			throw new IllegalStateException("Invite must target exactly one identity");
		}
	}


}

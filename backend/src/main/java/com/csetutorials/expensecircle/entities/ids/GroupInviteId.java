package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class GroupInviteId implements Serializable {

	@Id
	Long groupId;
	@Id
	Long inviteId;

}

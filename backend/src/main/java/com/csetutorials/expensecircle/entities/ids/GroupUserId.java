package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GroupUserId {

	@Id
	private long groupId;

	@Id
	private String userId;

}

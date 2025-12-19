package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GroupUserId implements Serializable {

	@Id
	private Long groupId;

	@Id
	private String userId;

}

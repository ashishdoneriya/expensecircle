package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class ExpenseTagId implements Serializable {

	@Id
	private Long groupId;
	@Id
	private Long expenseId;
	@Id
	private Long tagId;

}

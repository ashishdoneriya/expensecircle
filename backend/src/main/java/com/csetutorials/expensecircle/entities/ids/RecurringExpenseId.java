package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class RecurringExpenseId implements Serializable {

	@Id
	private String groupId;

	@Id
	private String recurringId;

}
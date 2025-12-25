package com.csetutorials.expensecircle.entities.ids;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseIdWrapper implements Serializable {

	@Id
	private String groupId;
	@Id
	private String expenseId;

}

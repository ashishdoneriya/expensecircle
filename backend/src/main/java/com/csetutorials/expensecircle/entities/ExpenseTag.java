package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.ExpenseTagId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "expenseTags")
@IdClass(ExpenseTagId.class)
public class ExpenseTag {

	@Id
	private Long groupId;
	@Id
	private Long expenseId;
	@Id
	private Long tagId;

}

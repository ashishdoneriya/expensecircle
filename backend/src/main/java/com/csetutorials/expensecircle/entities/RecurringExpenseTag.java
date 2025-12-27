package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.RecurringExpenseTagId;
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
@Table(name = "recurringExpenseTags")
@IdClass(RecurringExpenseTagId.class)
public class RecurringExpenseTag {

	@Id
	private String groupId;
	@Id
	private String recurringId;
	@Id
	private String tagId;

}

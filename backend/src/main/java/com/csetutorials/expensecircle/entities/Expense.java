package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.ExpenseIdWrapper;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "expenses")
@IdClass(ExpenseIdWrapper.class)
public class Expense {

	@Id
	private String groupId;
	@Id
	private String expenseId;

	@Column(nullable = false)
	private long timestamp;

	@Column(nullable = false)
	private short year;

	@Column(nullable = false)
	private byte month;

	@Column(nullable = false)
	private byte dayOfMonth;

	@Column(nullable = false)
	private long amount;

	@Column(length = 200)
	private String description;

	@Column(nullable = false)
	private String categoryId;

	@Column(length = 254)
	private String ownerUserId;

	@Column(length = 254)
	private String lastChangedByUserId;

	// Tags are handled separately

}

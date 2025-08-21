package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.ExpenseIdWrapper;
import com.csetutorials.expensecircle.jsonconverters.SetLongToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
	private long groupId;
	@Id
	private long expenseId;

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
	private long categoryId;

	@Column(nullable = false)
	@Convert(converter = SetLongToJsonConverter.class)
	private Set<Long> tags;

	@Column(length = 254)
	private String ownerUserId;

	@Column(length = 254)
	private String lastChangedByUserId;

}

package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.GroupCategoryId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "groupCategories")
@IdClass(GroupCategoryId.class)
@DynamicUpdate
public class GroupCategory {

	@Id
	private String groupId;

	@Id
	private String categoryId;

	@Column(length = 100)
	private String categoryName;

	@Column
	private long orderNumber;

}

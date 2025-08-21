package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.GroupTagId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "groupTags")
@IdClass(GroupTagId.class)
public class GroupTag {

	@Id
	private long groupId;

	@Id
	private long tagId;

	@Column
	private String tagName;

	@Column
	private long orderNumber;

}

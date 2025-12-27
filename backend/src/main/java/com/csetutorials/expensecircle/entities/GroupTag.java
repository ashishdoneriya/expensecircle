package com.csetutorials.expensecircle.entities;

import com.csetutorials.expensecircle.entities.ids.GroupTagId;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "groupTags")
@IdClass(GroupTagId.class)
@Builder
public class GroupTag {

	@Id
	private String groupId;

	@Id
	private String tagId;

	@Column
	private String tagName;

	@Column
	private long orderNumber;

}

package com.csetutorials.expensecircle.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {

	@Id
	private Long groupId;

	@Column(length = 200)
	private String groupName;

}

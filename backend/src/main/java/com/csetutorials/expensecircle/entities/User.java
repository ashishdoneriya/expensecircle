package com.csetutorials.expensecircle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@DynamicUpdate
public class User {

	@Id
	private String userId;

	private String email;

	@Column(nullable = false)
	private String name;

	private String pictureUrl;

}

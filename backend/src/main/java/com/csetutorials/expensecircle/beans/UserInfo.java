package com.csetutorials.expensecircle.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

	private String userId;

	private String name;

	private String email;

	private String picture;

}

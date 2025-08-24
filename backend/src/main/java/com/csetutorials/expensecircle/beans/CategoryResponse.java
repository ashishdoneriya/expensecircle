package com.csetutorials.expensecircle.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

	private long categoryId;

	private String categoryName;

	private boolean isDeleted;

}

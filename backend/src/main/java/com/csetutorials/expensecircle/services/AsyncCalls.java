package com.csetutorials.expensecircle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class AsyncCalls {

	@Autowired
	private DeleteService deleteService;

	public void deleteGroup(String groupId) {
		deleteService.deleteGroup(groupId);
	}

	public void deleteCategory(String groupId, String categoryId) {
		deleteService.deleteCategory(groupId, categoryId);
	}

	public void deleteTag(String groupId, String tagId) {
		deleteService.deleteTag(groupId, tagId);
	}

	public void deleteExpense(String groupId, String expenseId) {
		deleteService.deleteExpense(groupId, expenseId);
	}

}

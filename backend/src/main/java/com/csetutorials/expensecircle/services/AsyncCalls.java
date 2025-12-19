package com.csetutorials.expensecircle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class AsyncCalls {

	@Autowired
	private UserService userService;

	@Autowired
	private DeleteService deleteService;

	public void updateUserInfo(String userId, String name, String pictureUrl) {
		userService.save(userId, name, pictureUrl);
	}

	public void deleteGroup(long groupId) {
		deleteService.deleteGroup(groupId);
	}

	public void deleteCategory(long groupId, long categoryId) {
		deleteService.deleteCategory(groupId, categoryId);
	}

	public void deleteTag(long groupId, long tagId) {
		deleteService.deleteTag(groupId, tagId);
	}

	public void deleteExpense(long groupId, long expenseId) {
		deleteService.deleteExpense(groupId, expenseId);
	}

}

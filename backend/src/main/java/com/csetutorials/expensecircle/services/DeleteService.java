package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DeleteService {

	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private GroupCategoryRepository groupCategoryRepository;
	@Autowired
	private GroupTagRepository groupTagRepository;
	@Autowired
	private GroupUserMembershipRepository groupUserMembershipRepository;
	@Autowired
	private ExpenseTagRepository expenseTagRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private RecurringExpenseTagRepository recurringExpenseTagRepository;
	@Autowired
	private RecurringExpenseRepository recurringExpenseRepository;

	public void deleteGroup(String groupId) {
		recurringExpenseTagRepository.deleteByGroupId(groupId);
		recurringExpenseRepository.deleteByGroupId(groupId);
		expenseTagRepository.deleteByGroupId(groupId);
		expenseRepository.deleteByGroupId(groupId);
		groupCategoryRepository.deleteByGroupId(groupId);
		groupTagRepository.deleteByGroupId(groupId);
		groupUserMembershipRepository.deleteByGroupId(groupId);
		groupRepository.deleteByGroupId(groupId);
	}

	public void deleteCategory(String groupId, String categoryId) {
		recurringExpenseRepository.deleteByCategoryId(groupId, categoryId);
		expenseRepository.deleteByCategoryId(groupId, categoryId);
		groupCategoryRepository.deleteByCategoryId(groupId, categoryId);
	}

	public void deleteTag(String groupId, String tagId) {
		recurringExpenseTagRepository.deleteByGroupIdAndTagId(groupId, tagId);
		expenseTagRepository.deleteByGroupIdAndTagId(groupId, tagId);
		groupTagRepository.deleteByGroupIdAndTagId(groupId, tagId);
	}

	public void deleteExpense(String groupId, String expenseId) {
		expenseTagRepository.deleteByGroupIdAndExpenseId(groupId, expenseId);
		expenseRepository.deleteByGroupIdAndExpenseId(groupId, expenseId);
	}

}

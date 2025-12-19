package com.csetutorials.expensecircle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csetutorials.expensecircle.repositories.ExpenseRepository;
import com.csetutorials.expensecircle.repositories.ExpenseTagRepository;
import com.csetutorials.expensecircle.repositories.GroupCategoryRepository;
import com.csetutorials.expensecircle.repositories.GroupRepository;
import com.csetutorials.expensecircle.repositories.GroupTagRepository;
import com.csetutorials.expensecircle.repositories.GroupUserMembershipRepository;

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

	public void deleteGroup(long groupId) {
		groupRepository.deleteByGroupId(groupId);
		groupCategoryRepository.deleteByGroupId(groupId);
		groupTagRepository.deleteByGroupId(groupId);
		groupUserMembershipRepository.deleteByGroupId(groupId);
		expenseTagRepository.deleteByGroupId(groupId);
		expenseRepository.deleteByGroupId(groupId);
	}

	public void deleteCategory(long groupId, long categoryId) {
		groupCategoryRepository.deleteByCategoryId(groupId, categoryId);
		expenseRepository.deleteByCategoryId(groupId, categoryId);
	}

	public void deleteTag(long groupId, long tagId) {
		groupTagRepository.deleteByGroupIdAndTagId(groupId, tagId);
		expenseTagRepository.deleteByGroupIdAndTagId(groupId, tagId);
	}

	public void deleteExpense(long groupId, long expenseId) {
		expenseRepository.deleteByGroupIdAndExpenseId(groupId, expenseId);
		expenseTagRepository.deleteByGroupIdAndExpenseId(groupId, expenseId);
	}

}

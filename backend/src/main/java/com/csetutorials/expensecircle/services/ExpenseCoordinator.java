package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.beans.UpdateExpenseRequest;
import com.csetutorials.expensecircle.dto.ExpenseResponseDto;
import com.csetutorials.expensecircle.entities.Expense;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseCoordinator {

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private GroupTagService groupTagService;

	@Autowired
	private ExpenseTagService expenseTagService;

	public void addExpense(long groupId, AddExpenseRequest request, @NonNull String ownerUserId) {
		
		expenseService.addExpense(groupId, request, ownerUserId);

		if (request.getTags() == null) {
			request.setTags(Collections.emptySet());
		}
		groupTagService.verifyTags(groupId, request.getTags());
		List<Long> newTags = groupTagService.addTags(groupId, request.getNewTags()).stream().map(obj -> obj.getTagId()).toList();
		List<Long> finalTagsList = new ArrayList<>();
		finalTagsList.addAll(request.getTags());
		finalTagsList.addAll(newTags);
		expenseTagService.addAll(groupId, request.getTimestamp(), finalTagsList);
	}

	public void updateExpense(long groupId, String email, UpdateExpenseRequest request) {
		
		expenseService.updateExpense(groupId, email, request);

		if (request.getTags() == null) {
			request.setTags(Collections.emptySet());
		}
		groupTagService.verifyTags(groupId, request.getTags());
		List<Long> newTags = groupTagService.addTags(groupId, request.getNewTags()).stream().map(obj -> obj.getTagId()).toList();
		List<Long> finalTagsList = new ArrayList<>();
		finalTagsList.addAll(request.getTags());
		finalTagsList.addAll(newTags);

		expenseTagService.deleteByGroupIdAndExpenseId(groupId, request.getExpenseId());
		expenseTagService.addAll(groupId, request.getExpenseId(), finalTagsList);
	}

	public Optional<ExpenseResponseDto> findByGroupIdAndExpenseId(long groupId, long expenseId) {
		Optional<Expense> opt = expenseService.findByGroupIdAndExpenseId(groupId, expenseId);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		Expense e = opt.get();
		List<Long> tagIds = expenseTagService.findByGroupIdAndExpenseId(groupId, expenseId);
		return Optional.of(new ExpenseResponseDto(e.getExpenseId(), e.getTimestamp(), e.getCategoryId(), e.getAmount(), e.getDescription(), tagIds, e.getOwnerUserId(), e.getLastChangedByUserId()));
	}

}

package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.dto.ExpenseRequest;
import com.csetutorials.expensecircle.dto.ExpenseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCoordinator {

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private GroupTagService groupTagService;

	@Autowired
	private ExpenseTagService expenseTagService;

	@Transactional
	public void addExpense(String groupId, ExpenseRequest request) {
		String expenseId = expenseService.addExpense(groupId, request);
		List<String> tagIds = groupTagService.resolveTagIds(groupId, request.getTags());
		expenseTagService.addAll(groupId, expenseId, tagIds);
	}

	@Transactional
	public void updateExpense(String groupId, String expenseId, ExpenseRequest request) {
		expenseService.updateExpense(groupId, expenseId, request);
		expenseTagService.deleteByGroupIdAndExpenseId(groupId, expenseId);
		List<String> tagIds = groupTagService.resolveTagIds(groupId, request.getTags());
		expenseTagService.addAll(groupId, expenseId, tagIds);
	}

	public Optional<ExpenseResponseDto> findByGroupIdAndExpenseId(String groupId, String expenseId) {
		return expenseService.findByGroupIdAndExpenseId(groupId, expenseId)
			.map(e -> {
				List<String> tagIds = expenseTagService.findByGroupIdAndExpenseId(groupId, expenseId);
				return new ExpenseResponseDto(
					e.getExpenseId(), e.getTimestamp(), e.getCategoryId(),
					e.getAmount(), e.getDescription(), tagIds, e.getCreatedBy(),
					e.getUpdatedBy(), e.getCreatedAt(), e.getUpdatedAt());
			});
	}

}
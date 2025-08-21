package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.beans.UpdateExpenseRequest;
import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.repositories.ExpenseRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseCoordinator {

	@Autowired
	private ExpenseRepository repo;

	@Autowired
	private GroupTagService tagService;


	public void addExpense(long groupId, AddExpenseRequest request, @NonNull String ownerUserId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getTimestamp());
		if (request.getTags() == null) {
			request.setTags(Collections.emptySet());
		}
		updateFinalTags(groupId, request.getTags(), request.getNewTags());
		Expense expense = Expense.builder()
			.groupId(groupId)
			.expenseId(calendar.getTimeInMillis())
			.year((short) calendar.get(Calendar.YEAR))
			.month((byte) (calendar.get(Calendar.MONTH) + 1))
			.dayOfMonth((byte) calendar.get(Calendar.DAY_OF_MONTH))
			.amount(request.getAmount())
			.description(request.getDescription())
			.ownerUserId(ownerUserId)
			.lastChangedByUserId(ownerUserId)
			.categoryId(request.getCategoryId())
			.tags(request.getTags())
			.timestamp(request.getTimestamp())
			.build();
		repo.save(expense);
	}

	private void updateFinalTags(long groupId, Set<Long> tags, Set<String> newTags) {
		long time = System.currentTimeMillis();
		for (String newTag : newTags) {
			tags.add(time);
			tagService.addTag(groupId, time, newTag);
			time++;
		}
	}

	public Expense get(long groupId, long expenseId) {
		Optional<Expense> opt = repo.findByGroupIdAndExpenseId(groupId, expenseId);
		return opt.orElse(null);
	}

	public void updateExpense(long groupId, String email, UpdateExpenseRequest request) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getExpenseId());

		Optional<Expense> expenseOpt = repo.findByGroupIdAndExpenseId(groupId, request.getExpenseId());
		if (expenseOpt.isEmpty()) {
			return;
		}
		updateFinalTags(groupId, request.getTags(), request.getNewTags());
		Expense expense = expenseOpt.get();
		calendar.setTimeInMillis(request.getNewTimestamp());
		expense.setYear((short) calendar.get(Calendar.YEAR));
		expense.setMonth((byte) (calendar.get(Calendar.MONTH) + 1));
		expense.setDayOfMonth((byte) calendar.get(Calendar.DAY_OF_MONTH));
		expense.setAmount(request.getAmount());
		expense.setCategoryId(request.getCategoryId());
		expense.setLastChangedByUserId(email);
		expense.setDescription(request.getDescription());
		expense.setTimestamp(request.getNewTimestamp());
		if (request.getTags() != null) {
			expense.setTags(request.getTags());
		}
		repo.save(expense);
	}

	public void deleteExpense(long groupId, long expenseId) {
		repo.deleteExpense(groupId, expenseId);
	}

	public List<ExpenseProjection> listExpenses(long groupId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndYearAndMonthAndDayOfMonth(groupId, year, month, dayOfMonth);
	}

	public List<ExpenseProjection> listExpenses(long groupId, long categoryId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndCategoryIdAndYearAndMonthAndDayOfMonth(groupId, categoryId, year, month, dayOfMonth);
	}

	public void deleteExpenses(long groupId) {
		repo.deleteExpenses(groupId);
	}

	public void deleteCategory(long groupId, long categoryId) {
		repo.deleteCategory(groupId, categoryId);
	}

	public void deleteTag(long groupId, long tagId) {
		repo.deleteTag(groupId, tagId);
	}
}

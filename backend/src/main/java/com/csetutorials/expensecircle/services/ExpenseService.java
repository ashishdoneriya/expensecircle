package com.csetutorials.expensecircle.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.beans.UpdateExpenseRequest;
import com.csetutorials.expensecircle.dto.ExpenseResponseDto;
import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.repositories.ExpenseRepository;

import lombok.NonNull;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository repo;

	public void addExpense(long groupId, AddExpenseRequest request, @NonNull String ownerUserId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getTimestamp());
		long expenseId = calendar.getTimeInMillis();
		Expense expense = Expense.builder()
			.groupId(groupId)
			.expenseId(expenseId)
			.year((short) calendar.get(Calendar.YEAR))
			.month((byte) (calendar.get(Calendar.MONTH) + 1))
			.dayOfMonth((byte) calendar.get(Calendar.DAY_OF_MONTH))
			.amount(request.getAmount())
			.description(request.getDescription())
			.ownerUserId(ownerUserId)
			.lastChangedByUserId(ownerUserId)
			.categoryId(request.getCategoryId())
			.timestamp(request.getTimestamp())
			.build();
		repo.save(expense);
	} 

	public void updateExpense(long groupId, String email, UpdateExpenseRequest request) {
	
		Optional<Expense> expenseOpt = repo.findByGroupIdAndExpenseId(groupId, request.getExpenseId());
		if (expenseOpt.isEmpty()) {
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getExpenseId());
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
		repo.save(expense);
	}

	public Optional<Expense> findByGroupIdAndExpenseId(long groupId, long expenseId) {
		return repo.findByGroupIdAndExpenseId(groupId, expenseId);
	}

	public List<ExpenseProjection> listExpenses(long groupId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndYearAndMonthAndDayOfMonth(groupId, year, month, dayOfMonth);
	}

	public List<ExpenseProjection> listExpenses(long groupId, long categoryId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndCategoryIdAndYearAndMonthAndDayOfMonth(groupId, categoryId, year, month, dayOfMonth);
	}



}

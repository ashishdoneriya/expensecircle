package com.csetutorials.expensecircle.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.csetutorials.expensecircle.dto.AuditHistoryDto;
import com.csetutorials.expensecircle.entities.User;
import com.csetutorials.expensecircle.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.csetutorials.expensecircle.dto.ExpenseRequest;
import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.repositories.ExpenseRepository;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository repo;
	@Autowired
	private IdGenerator idGenerator;
	@Autowired
	private UserService userService;

	public String addExpense(String groupId, ExpenseRequest request) {
		String id = idGenerator.getStringId();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getTimestamp());
		Expense expense = Expense.builder()
			.groupId(groupId)
			.expenseId(id)
			.year((short) calendar.get(Calendar.YEAR))
			.month((byte) (calendar.get(Calendar.MONTH) + 1))
			.dayOfMonth((byte) calendar.get(Calendar.DAY_OF_MONTH))
			.amount(request.getAmount())
			.description(request.getDescription())
			.categoryId(request.getCategoryId())
			.timestamp(request.getTimestamp())
			.build();
		repo.save(expense);
		return id;
	} 

	public void updateExpense(String groupId, String expenseId, ExpenseRequest request) {
	
		Optional<Expense> expenseOpt = repo.findByGroupIdAndExpenseId(groupId, expenseId);
		if (expenseOpt.isEmpty()) {
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getTimestamp());
		Expense expense = expenseOpt.get();
		expense.setYear((short) calendar.get(Calendar.YEAR));
		expense.setMonth((byte) (calendar.get(Calendar.MONTH) + 1));
		expense.setDayOfMonth((byte) calendar.get(Calendar.DAY_OF_MONTH));
		expense.setAmount(request.getAmount());
		expense.setCategoryId(request.getCategoryId());
		expense.setDescription(request.getDescription());
		expense.setTimestamp(request.getTimestamp());
		repo.save(expense);
	}

	public Optional<Expense> findByGroupIdAndExpenseId(String groupId, String expenseId) {
		return repo.findByGroupIdAndExpenseId(groupId, expenseId);
	}

	public List<ExpenseProjection> listExpenses(String groupId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndYearAndMonthAndDayOfMonth(groupId, year, month, dayOfMonth);
	}

	public List<ExpenseProjection> listExpenses(String groupId, String categoryId, short year, byte month, byte dayOfMonth) {
		return repo.findByGroupIdAndCategoryIdAndYearAndMonthAndDayOfMonth(groupId, categoryId, year, month, dayOfMonth);
	}

	public AuditHistoryDto getAuditHistory(String groupId, String expenseId) {
		Expense expense = findByGroupIdAndExpenseId(
				groupId, expenseId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Expense not found or deleted"));

		AuditHistoryDto dto = new AuditHistoryDto();

		if (StringUtils.isNotBlank(expense.getCreatedBy())) {
			var optUser = userService.get(expense.getCreatedBy());
			if (optUser.isPresent()) {
				var user = optUser.get();
				dto.setCreatedByEmail(user.getEmail());
				dto.setCreatedByName(user.getName());
				dto.setCreatedAt(expense.getCreatedAt());
			}
		}
		if (StringUtils.isNotBlank(expense.getUpdatedBy())
			&& !expense.getCreatedAt().equals(expense.getUpdatedAt())) {
			var optUser = userService.get(expense.getUpdatedBy());
			if (optUser.isPresent()) {
				var user = optUser.get();
				dto.setUpdatedByEmail(user.getEmail());
				dto.setUpdatedByName(user.getName());
				dto.setUpdatedAt(expense.getUpdatedAt());
			}
		}
		return dto;
	}

}

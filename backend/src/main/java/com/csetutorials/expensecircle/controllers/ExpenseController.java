package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.beans.UpdateExpenseRequest;
import com.csetutorials.expensecircle.dto.ExpenseResponseDto;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.ExpenseService;
import com.csetutorials.expensecircle.services.LoggedInUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
@GroupMemberOnly
public class ExpenseController {

	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private AsyncCalls asyncCalls;

	@GetMapping
	public List<ExpenseProjection> listExpenses(
		@PathVariable("groupId") String groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month,
		@RequestParam("dayOfMonth") byte dayOfMonth,
		@RequestParam(name = "categoryId", required = false, defaultValue = "") String categoryId) {
		if (categoryId.trim().isEmpty()) {
			return expenseService.listExpenses(groupId, year, month, dayOfMonth);
		}
		return expenseService.listExpenses(groupId, categoryId, year, month, dayOfMonth);
	}

	@PostMapping
	public void createExpense(
		@PathVariable("groupId") String groupId,
		@RequestBody AddExpenseRequest form) {
		expenseCoordinator.addExpense(groupId, form, loggedInUserInfoService.getId());
	}

	@GetMapping("/{expenseId}")
	public ExpenseResponseDto getExpense(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId) {
		return expenseCoordinator.findByGroupIdAndExpenseId(groupId, expenseId).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Expense not found"));
	}

	@PutMapping("/{expenseId}")
	public void updateExpense(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId,
		@RequestBody UpdateExpenseRequest form) {
		expenseCoordinator.updateExpense(groupId, loggedInUserInfoService.getId(), form);
	}

	@DeleteMapping("/{expenseId}")
	public void deleteExpense(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId) {
		asyncCalls.deleteExpense(groupId, expenseId);
	}

}

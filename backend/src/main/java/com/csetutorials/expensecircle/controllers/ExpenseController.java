package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.beans.UpdateExpenseRequest;
import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.LoggedInUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
public class ExpenseController {

	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;

	@GetMapping
	public List<ExpenseProjection> listExpenses(
		@PathVariable("groupId") long groupId,
		@RequestParam("year") short year,
		@RequestParam("month") byte month,
		@RequestParam("dayOfMonth") byte dayOfMonth,
		@RequestParam(name = "categoryId", required = false, defaultValue = "") String sCategoryId) {
		if (sCategoryId.trim().isEmpty()) {
			return expenseCoordinator.listExpenses(groupId, year, month, dayOfMonth);
		}
		return expenseCoordinator.listExpenses(groupId, Long.parseLong(sCategoryId), year, month, dayOfMonth);
	}

	@PostMapping
	public void createExpense(
		@PathVariable("groupId") long groupId,
		@RequestBody AddExpenseRequest form) {
		UserInfo userInfo = loggedInUserInfoService.getInfo();
		expenseCoordinator.addExpense(groupId, form, userInfo.getEmail());
	}

	@GetMapping("/{expenseId}")
	public Expense getExpense(
		@PathVariable("groupId") long groupId,
		@PathVariable("expenseId") long expenseId) {
		return expenseCoordinator.get(groupId, expenseId);
	}

	@PutMapping("/{expenseId}")
	public void updateExpense(
		@PathVariable("groupId") long groupId,
		@PathVariable("expenseId") long expenseId,
		@RequestBody UpdateExpenseRequest form) {

		UserInfo userInfo = loggedInUserInfoService.getInfo();
		expenseCoordinator.updateExpense(groupId, userInfo.getEmail(), form);
	}

	@DeleteMapping("/{expenseId}")
	public void deleteExpense(
		@PathVariable("groupId") long groupId,
		@PathVariable("expenseId") long expenseId) {
		expenseCoordinator.deleteExpense(groupId, expenseId);
	}

}

package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.dto.AuditHistoryDto;
import com.csetutorials.expensecircle.dto.ExpenseRequest;
import com.csetutorials.expensecircle.dto.ExpenseResponseDto;
import com.csetutorials.expensecircle.entities.Expense;
import com.csetutorials.expensecircle.entities.User;
import com.csetutorials.expensecircle.projection.ExpenseProjection;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.ExpenseService;
import com.csetutorials.expensecircle.services.UserService;
import com.csetutorials.expensecircle.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
@GroupMemberOnly
public class ExpenseController {

	@Autowired
	private ExpenseCoordinator expenseCoordinator;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private AsyncCalls asyncCalls;
	@Autowired
	private UserService userService;

	@GetMapping
	public List<ExpenseProjection> list(
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
	public void create(
		@PathVariable("groupId") String groupId,
		@RequestBody ExpenseRequest form) {
		expenseCoordinator.addExpense(groupId, form);
	}

	@GetMapping("/{expenseId}")
	public ExpenseResponseDto get(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId) {
		return expenseCoordinator.findByGroupIdAndExpenseId(
			groupId, expenseId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Expense not found"));
	}

	@PutMapping("/{expenseId}")
	public void update(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId,
		@RequestBody ExpenseRequest form) {
		expenseCoordinator.updateExpense(groupId, expenseId, form);
	}

	@DeleteMapping("/{expenseId}")
	public void delete(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId) {
		asyncCalls.deleteExpense(groupId, expenseId);
	}

	@GetMapping("/{expenseId}/audit-details")
	public AuditHistoryDto getAuditDetails(
		@PathVariable("groupId") String groupId,
		@PathVariable("expenseId") String expenseId) {

		return expenseService.getAuditHistory(groupId, expenseId);
	}

}

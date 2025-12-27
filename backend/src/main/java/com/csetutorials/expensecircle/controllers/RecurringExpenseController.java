package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.dto.AuditHistoryDto;
import com.csetutorials.expensecircle.dto.RecurringExpenseDetailResponseDto;
import com.csetutorials.expensecircle.dto.RecurringExpenseRequest;
import com.csetutorials.expensecircle.projection.RecurringExpenseSummaryProjection;
import com.csetutorials.expensecircle.services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/recurring-expenses")
public class RecurringExpenseController {

	@Autowired
	private RecurringExpenseService service;

	@PostMapping
	public void create(@PathVariable String groupId, @RequestBody RecurringExpenseRequest request) {
		service.create(groupId, request);
	}

	@PutMapping("/{recurringId}")
	public void update(
		@PathVariable String groupId,
		@PathVariable String recurringId,
		@RequestBody RecurringExpenseRequest request) {
		service.update(groupId, recurringId, request);
	}

	@GetMapping("/{recurringId}")
	public RecurringExpenseDetailResponseDto get(@PathVariable String groupId,
												 @PathVariable String recurringId) {
		return service.findByGroupIdAndRecurringId(groupId, recurringId)
			.orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Recurring Expense not found"));
	}

	@DeleteMapping("/{recurringId}")
	public void delete(@PathVariable String groupId,
					   @PathVariable String recurringId) {
		service.delete(groupId, recurringId);
	}

	@GetMapping
	public List<RecurringExpenseSummaryProjection> list(@PathVariable String groupId) {
		return service.list(groupId);
	}

	@GetMapping("/{recurringId}/audit-details")
	public AuditHistoryDto getAuditDetails(
		@PathVariable("groupId") String groupId,
		@PathVariable("recurringId") String recurringId) {

		return service.getAuditHistory(groupId, recurringId);
	}

}
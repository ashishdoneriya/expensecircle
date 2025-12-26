package com.csetutorials.expensecircle.schedulers;

import com.csetutorials.expensecircle.beans.AddExpenseRequest;
import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.ExpenseService;
import com.csetutorials.expensecircle.services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RecurringExpenseProcess {

	@Autowired
	private RecurringExpenseService recurringExpenseService;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;
	@Autowired
	private ExpenseService expenseService;

	@Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
	public void process() {
		List<RecurringExpense> recurringExpenseList;
		do {
			recurringExpenseList = recurringExpenseService.list();
			for (RecurringExpense recurringExpense: recurringExpenseList) {
				processSingleRecurringTask(recurringExpense, System.currentTimeMillis());
			}
		} while (!recurringExpenseList.isEmpty());
	}

	private void processSingleRecurringTask(RecurringExpense task, long now) {
		long currentTrigger = task.getNextTriggerEpoch();

		// Safety Guard: Stop infinite loops if data is corrupt
		if (currentTrigger <= 0) return;

		while (currentTrigger <= now) {
			AddExpenseRequest request = new AddExpenseRequest();
			request.setAmount(task.getAmount());
			request.setDescription(task.getDescription());
			request.setCategoryId(task.getCategoryId());
			request.setTimestamp(currentTrigger);

			expenseCoordinator.addExpense(task.getGroupId(), request, "SYSTEM");

			// Move to next occurrence
			currentTrigger = calculateNextEpoch(task, currentTrigger);
		}

		recurringExpenseService.updateNextTriggerEpoch(task.getGroupId(), task.getRecurringId(), currentTrigger);
	}

	private long calculateNextEpoch(RecurringExpense task, long currentEpoch) {
		ZonedDateTime zdt = Instant.ofEpochMilli(currentEpoch).atZone(ZoneId.systemDefault());

		zdt = switch (task.getFrequency()) {
			case DAILY -> zdt.plusDays(1);
			case WEEKLY -> zdt.plusWeeks(1);
			case MONTHLY -> (task.getDayOfMonth() == 99)
				? zdt.plusMonths(1).with(java.time.temporal.TemporalAdjusters.lastDayOfMonth())
				: zdt.plusMonths(1);
		};
		return zdt.toInstant().toEpochMilli();
	}

}

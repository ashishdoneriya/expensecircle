package com.csetutorials.expensecircle.schedulers;

import com.csetutorials.expensecircle.beans.DayPeriod;
import com.csetutorials.expensecircle.beans.RecurringFrequency;
import com.csetutorials.expensecircle.dto.ExpenseRequest;
import com.csetutorials.expensecircle.dto.RecurringExpenseRequest;
import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.services.ExpenseService;
import com.csetutorials.expensecircle.services.ExpenseTagService;
import com.csetutorials.expensecircle.services.RecurringExpenseService;
import com.csetutorials.expensecircle.services.RecurringExpenseTagService;
import com.csetutorials.expensecircle.utilities.DateUtils;
import java.time.temporal.TemporalAdjusters;
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
	private RecurringExpenseTagService tagService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private ExpenseTagService expenseTagService;

	@Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
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

		if (currentTrigger <= 0) {
			currentTrigger = calculateInitialEpoch(task);
		}

		String groupId = task.getGroupId();
		String recurringId = task.getRecurringId();
		while (currentTrigger <= now) {
			ExpenseRequest request = new ExpenseRequest();
			request.setTimezone(task.getTimezone());
			request.setAmount(task.getAmount());
			request.setDescription(task.getDescription());
			request.setCategoryId(task.getCategoryId());
			request.setTimestamp(currentTrigger);
			String expenseId = expenseService.addExpense(groupId, request, task.getCreatedBy());
			expenseTagService.addAll(groupId, expenseId,
				tagService.findByGroupIdAndRecurringId(groupId, recurringId));
			// Move to next occurrence
			currentTrigger = calculateNextEpoch(task, currentTrigger);
		}

		recurringExpenseService.updateNextTriggerEpoch(groupId, recurringId, currentTrigger);
	}

	private long calculateNextEpoch(RecurringExpense task, long epoch) {
		ZonedDateTime zdt = ZonedDateTime.ofInstant(
			Instant.ofEpochMilli(epoch), ZoneId.of(task.getTimezone()));

		zdt = switch (task.getFrequency()) {
			case DAILY -> zdt.plusDays(1);
			case WEEKLY -> zdt.plusWeeks(1);
			case MONTHLY -> {
				zdt = zdt.plusMonths(1);
				if (task.getDayOfMonth() > zdt.toLocalDate().lengthOfMonth()) {
					yield zdt.with(TemporalAdjusters.lastDayOfMonth());
				} else {
					yield zdt.withDayOfMonth(task.getDayOfMonth());
				}
			}
		};
		return zdt.toInstant().toEpochMilli();
	}

	private long calculateInitialEpoch(RecurringExpense request) {
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(request.getTimezone()));
		int hour = request.getExecutionTimeHour();
		if (request.getDayPeriod() == DayPeriod.PM) {
			hour += 12;
		}
		zdt = zdt.withNano(0)
			.withSecond(0)
			.withMinute(request.getExecutionTimeMinute())
			.withHour(hour);

		if (RecurringFrequency.WEEKLY == request.getFrequency()) {
			zdt = zdt.with(
				TemporalAdjusters
					.nextOrSame(
						DateUtils.WEEK_MAP.get(request.getDayOfWeek())));

		} else if (RecurringFrequency.MONTHLY == request.getFrequency()) {
			if (request.getDayOfMonth() > zdt.toLocalDate().lengthOfMonth()) {
				zdt = zdt.with(TemporalAdjusters.lastDayOfMonth());
			} else {
				zdt = zdt.withDayOfMonth(request.getDayOfMonth());
			}
		}

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(request.getTimezone()));

		if (zdt.isBefore(now)) {
			zdt = switch (request.getFrequency()) {
				case DAILY -> zdt.plusDays(1);
				case WEEKLY -> zdt.plusWeeks(1);
				case MONTHLY -> {
					zdt = zdt.plusMonths(1);
					if (request.getDayOfMonth() > zdt.toLocalDate().lengthOfMonth()) {
						yield zdt.with(TemporalAdjusters.lastDayOfMonth());
					} else {
						yield zdt.withDayOfMonth(request.getDayOfMonth());
					}
				}
			};
		}

		return zdt.toInstant().toEpochMilli();
	}

}
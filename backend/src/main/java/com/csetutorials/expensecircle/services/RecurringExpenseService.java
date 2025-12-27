package com.csetutorials.expensecircle.services;


import com.csetutorials.expensecircle.beans.*;
import com.csetutorials.expensecircle.dto.AuditHistoryDto;
import com.csetutorials.expensecircle.dto.RecurringExpenseDetailResponseDto;
import com.csetutorials.expensecircle.dto.RecurringExpenseRequest;
import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.projection.RecurringExpenseSummaryProjection;
import com.csetutorials.expensecircle.repositories.RecurringExpenseRepository;
import com.csetutorials.expensecircle.utilities.DateUtils;
import com.csetutorials.expensecircle.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class RecurringExpenseService {

	@Autowired
	private RecurringExpenseRepository repo;
	@Autowired
	private RecurringExpenseTagService tagService;
	@Autowired
	private IdGenerator idGenerator;
	@Autowired
	private GroupTagService groupTagService;
	@Autowired
	private UserService userService;

	public void create(String groupId, RecurringExpenseRequest request) {
		String recurringId = idGenerator.getStringId();
		repo.save(RecurringExpense.builder()
			.groupId(groupId)
			.recurringId(idGenerator.getStringId())
			.categoryId(request.getCategoryId())
			.amount(request.getAmount())
			.description(request.getDescription())
			.frequency(request.getFrequency())
			.dayOfMonth(request.getDayOfMonth())
			.dayOfWeek(request.getDayOfWeek())
			.executionTimeHour(request.getExecutionTimeHour())
			.executionTimeMinute(request.getExecutionTimeMinute())
			.dayPeriod(request.getDayPeriod())
			.nextTriggerEpoch(calculateInitialEpoch(request))
			.build());
		tagService.addAll(groupId, recurringId,
			groupTagService.resolveTagIds(groupId, request.getTags()));
	}

	public void update(String groupId, String recurringId, RecurringExpenseRequest request) {
		repo.save(RecurringExpense.builder()
			.groupId(groupId)
			.recurringId(recurringId)
			.categoryId(request.getCategoryId())
			.amount(request.getAmount())
			.description(request.getDescription())
			.frequency(request.getFrequency())
			.dayOfMonth(request.getDayOfMonth())
			.dayOfWeek(request.getDayOfWeek())
			.executionTimeHour(request.getExecutionTimeHour())
			.executionTimeMinute(request.getExecutionTimeMinute())
			.dayPeriod(request.getDayPeriod())
			.nextTriggerEpoch(calculateInitialEpoch(request))
			.build());
		tagService.deleteByGroupIdAndRecurringId(groupId, recurringId);
		tagService.addAll(groupId, recurringId,
			groupTagService.resolveTagIds(groupId, request.getTags()));
	}

	public Optional<RecurringExpenseDetailResponseDto> findByGroupIdAndRecurringId(
		String groupId, String recurringId) {
		Optional<RecurringExpenseDetailResponseDto> opt = repo.findByGroupIdAndRecurringId(
			groupId, recurringId)
			.map(this::convert);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		RecurringExpenseDetailResponseDto dto = opt.get();
		dto.setTags(tagService.findByGroupIdAndRecurringId(groupId, recurringId));
		return Optional.of(dto);
	}

	public void delete(String groupId, String recurringId) {
		repo.deleteByGroupIdAndRecurringId(groupId, recurringId);
	}

	public List<RecurringExpenseSummaryProjection> list(String groupId) {
		return repo.findByGroupId(groupId);
	}

	/**
	 * This method is supposed to be called from scheduler
	 * @return First 100 List<RecurringExpense>
	 */
	public List<RecurringExpense> list() {
		long time = System.currentTimeMillis();
		return repo.findDue(time, Pageable.ofSize(100));
	}

	private RecurringExpenseDetailResponseDto convert(RecurringExpense e) {
		return RecurringExpenseDetailResponseDto.builder()
			.recurringId(e.getRecurringId())
			.categoryId(e.getCategoryId())
			.amount(e.getAmount())
			.description(e.getDescription())
			.frequency(e.getFrequency())
			.dayOfMonth(e.getDayOfMonth())
			.dayOfWeek(e.getDayOfWeek())
			.dayPeriod(e.getDayPeriod())
			.executionTimeHour(e.getExecutionTimeHour())
			.executionTimeMinute(e.getExecutionTimeMinute())
			.createdBy(e.getCreatedBy())
			.updatedBy(e.getUpdatedBy())
			.createdAt(e.getCreatedAt())
			.updatedAt(e.getUpdatedAt())
			.build();
	}


	/**
	 * This method is supposed to be called from scheduler
	 */
	public void updateNextTriggerEpoch(String groupId, String recurringId, long nextTriggerEpoch) {
		repo.updateNextTriggerEpoch(groupId, recurringId, nextTriggerEpoch);
	}

	private long calculateInitialEpoch(RecurringExpenseRequest request) {

		Calendar initial = Calendar.getInstance();
		initial.set(Calendar.SECOND, 0);
		initial.set(Calendar.MILLISECOND, 0);
		initial.set(Calendar.MINUTE, request.getExecutionTimeMinute());
		initial.set(Calendar.HOUR, request.getExecutionTimeHour());
		initial.set(Calendar.AM_PM, request.getDayPeriod() == DayPeriod.AM ? Calendar.AM : Calendar.PM);

		if (RecurringFrequency.WEEKLY == request.getFrequency()) {
			initial.set(Calendar.DAY_OF_WEEK, DateUtils.WEEK_MAP.get(request.getDayOfWeek()));
		} else if (RecurringFrequency.MONTHLY == request.getFrequency()) {
			if (request.getDayOfMonth() == 99) {
				initial.set(Calendar.DAY_OF_MONTH, YearMonth.now().lengthOfMonth());
			} else {
				initial.set(Calendar.DAY_OF_MONTH, request.getDayOfMonth());
			}
		}

		if (initial.compareTo(Calendar.getInstance()) < 0) {
			if (RecurringFrequency.WEEKLY == request.getFrequency()) {
				initial.add(Calendar.DAY_OF_MONTH, 7);
			} else if (RecurringFrequency.MONTHLY == request.getFrequency()) {
				initial.add(Calendar.MONTH, 1);
			}
		}

		return initial.getTimeInMillis();
	}

	public AuditHistoryDto getAuditHistory(String groupId, String recurringId) {
		RecurringExpense expense = repo.findByGroupIdAndRecurringId(
				groupId, recurringId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Recurring Expense not found or deleted"));

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
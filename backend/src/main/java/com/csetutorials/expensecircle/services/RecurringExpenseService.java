package com.csetutorials.expensecircle.services;


import com.csetutorials.expensecircle.beans.RecurringExpenseDetailResponseDto;
import com.csetutorials.expensecircle.beans.RecurringExpenseRequestDto;
import com.csetutorials.expensecircle.entities.RecurringExpense;
import com.csetutorials.expensecircle.projection.RecurringExpenseSummaryProjection;
import com.csetutorials.expensecircle.repositories.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecurringExpenseService {

	@Autowired
	private RecurringExpenseRepository repo;

	public List<RecurringExpenseSummaryProjection> list(long groupId) {
		return repo.findByGroupId(groupId);
	}

	/**
	 * This method is supoosed to be called from scheduler
	 * @return First 100 List<RecurringExpense>
	 */
	public List<RecurringExpense> list() {
		long time = System.currentTimeMillis();
		return repo.findDue(time, Pageable.ofSize(100));
	}

	public Optional<RecurringExpenseDetailResponseDto> getDto(long groupId, long recurringId) {
		return repo.findByGroupIdAndRecurringId(groupId, recurringId).map(this::convert);
	}

	public RecurringExpenseDetailResponseDto convert(RecurringExpense e) {
		return RecurringExpenseDetailResponseDto.builder()
			.recurringId(e.getRecurringId())
			.categoryId(e.getCategoryId())
			.amount(e.getAmount())
			.description(e.getDescription())
			.frequency(e.getFrequency())
			.dayOfMonth(e.getDayOfMonth())
			.dayOfWeek(e.getDayOfWeek())
			.executionTimeHour(e.getExecutionTimeHour())
			.executionTimeMinute(e.getExecutionTimeMinute())
			.build();
	}

	public void delete(long groupId, long recurringId) {
		repo.deleteRecord(groupId, recurringId);
	}

	public void updateNextTriggerEpoch(long groupId, long recurringId, long nextTriggerEpoch) {
		repo.updateNextTriggerEpoch(groupId, recurringId, nextTriggerEpoch);
	}

	public void create(RecurringExpenseRequestDto request) {

	}

}

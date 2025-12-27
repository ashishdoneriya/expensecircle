package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.entities.RecurringExpenseTag;
import com.csetutorials.expensecircle.repositories.RecurringExpenseTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RecurringExpenseTagService {

	@Autowired
	private RecurringExpenseTagRepository repo;

	public void addAll(String groupId, String recurringId, Collection<String> tagIds) {
		if (tagIds == null || tagIds.isEmpty()) {
			return;
		}
		repo.saveAll(
			tagIds.stream()
			.map(tagId -> new RecurringExpenseTag(groupId, recurringId, tagId))
			.toList()
		);
	}

    public void deleteByGroupIdAndRecurringId(String groupId, String recurringId) {
        repo.deleteByGroupIdAndRecurringId(groupId, recurringId);
    }

	public List<String> findByGroupIdAndRecurringId(String groupId, String recurringId) {
		return repo.findByGroupIdAndRecurringId(groupId, recurringId)
			.stream()
			.map(RecurringExpenseTag::getTagId)
			.toList();
	}

}

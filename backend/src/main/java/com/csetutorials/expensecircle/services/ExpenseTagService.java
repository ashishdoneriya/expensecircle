package com.csetutorials.expensecircle.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csetutorials.expensecircle.entities.ExpenseTag;
import com.csetutorials.expensecircle.repositories.ExpenseTagRepository;

@Service
public class ExpenseTagService {

	@Autowired
	private ExpenseTagRepository repo;

	public void addAll(long groupId, long expenseId, Collection<Long> tagIds) {
		repo.saveAll(
			tagIds.stream()
			.map(tagId -> new ExpenseTag(groupId, expenseId, tagId))
			.toList()
		);
	}

    public void deleteByGroupIdAndExpenseId(long groupId, long expenseId) {
        repo.deleteByGroupIdAndExpenseId(groupId, expenseId);
    }

	public List<Long> findByGroupIdAndExpenseId(long groupId, long expenseId) {
		return repo.findByGroupIdAndExpenseId(groupId, expenseId).stream().map(obj -> obj.getTagId()).toList();
	}

}

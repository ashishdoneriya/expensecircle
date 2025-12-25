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

	public void addAll(String groupId, String expenseId, Collection<String> tagIds) {
		repo.saveAll(
			tagIds.stream()
			.map(tagId -> new ExpenseTag(groupId, expenseId, tagId))
			.toList()
		);
	}

    public void deleteByGroupIdAndExpenseId(String groupId, String expenseId) {
        repo.deleteByGroupIdAndExpenseId(groupId, expenseId);
    }

	public List<String> findByGroupIdAndExpenseId(String groupId, String expenseId) {
		return repo.findByGroupIdAndExpenseId(groupId, expenseId).stream().map(ExpenseTag::getTagId).toList();
	}

}

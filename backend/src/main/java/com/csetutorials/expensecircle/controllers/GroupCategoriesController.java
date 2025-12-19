package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.beans.Name;
import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupCategory;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.GroupCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/categories")
@GroupMemberOnly
public class GroupCategoriesController {

	@Autowired
	private GroupCategoryService service;
	@Autowired
	private AsyncCalls asyncCalls;

	@GetMapping
	public List<GroupCategory> getCategories(@PathVariable("groupId") long groupId) {
		List<GroupCategory> list = service.getCategories(groupId);
		list.sort(Comparator.comparingLong(GroupCategory::getOrderNumber));
		return list;
	}

	@PostMapping
	public long addCategory(@PathVariable("groupId") long groupId, @Valid @RequestBody Name name) {
		long timestamp = System.currentTimeMillis();
		service.addCategory(groupId, timestamp, name.getName());
		return timestamp;
	}

	@PostMapping("/change-categories-order")
	public void changeCategoriesOrder(
		@PathVariable("groupId") long groupId,
		@RequestBody List<NewOrder> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		service.changeCategoriesOrder(groupId, list);
	}

	@PutMapping("/{categoryId}")
	public void renameCategory(
		@PathVariable("groupId") long groupId,
		@PathVariable("categoryId") long categoryId,
		@Valid @RequestBody Name name) {
		service.renameCategory(groupId, categoryId, name.getName());
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCategory(
		@PathVariable("groupId") long groupId,
		@PathVariable("categoryId") long categoryId) {
		asyncCalls.deleteCategory(groupId, categoryId);
	}


}

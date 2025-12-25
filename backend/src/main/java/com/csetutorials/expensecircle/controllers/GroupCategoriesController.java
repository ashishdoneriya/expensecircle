package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.beans.Name;
import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupCategory;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.GroupCategoryService;
import com.csetutorials.expensecircle.services.IdGenerator;
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
	@Autowired
	private IdGenerator idGenerator;

	@GetMapping
	public List<GroupCategory> getCategories(@PathVariable("groupId") String groupId) {
		List<GroupCategory> list = service.getCategories(groupId);
		list.sort(Comparator.comparingLong(GroupCategory::getOrderNumber));
		return list;
	}

	@PostMapping
	public String addCategory(@PathVariable("groupId") String groupId, @Valid @RequestBody Name name) {
		return service.addCategory(groupId, name.getName());
	}

	@PostMapping("/change-categories-order")
	public void changeCategoriesOrder(
		@PathVariable("groupId") String groupId,
		@RequestBody List<NewOrder> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		service.changeCategoriesOrder(groupId, list);
	}

	@PutMapping("/{categoryId}")
	public void renameCategory(
		@PathVariable("groupId") String groupId,
		@PathVariable("categoryId") String categoryId,
		@Valid @RequestBody Name name) {
		service.renameCategory(groupId, categoryId, name.getName());
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCategory(
		@PathVariable("groupId") String groupId,
		@PathVariable("categoryId") String categoryId) {
		asyncCalls.deleteCategory(groupId, categoryId);
	}


}

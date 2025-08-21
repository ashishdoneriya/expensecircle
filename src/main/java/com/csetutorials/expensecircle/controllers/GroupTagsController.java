package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.beans.Name;
import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupTag;
import com.csetutorials.expensecircle.services.ExpenseCoordinator;
import com.csetutorials.expensecircle.services.GroupTagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/tags")
public class GroupTagsController {

	@Autowired
	private GroupTagService service;
	@Autowired
	private ExpenseCoordinator expenseCoordinator;

	@GetMapping
	public List<GroupTag> getTags(@PathVariable("groupId") long groupId) {
		List<GroupTag> list = service.getTags(groupId);
		list.sort(Comparator.comparingLong(GroupTag::getOrderNumber));
		return list;
	}

	@PostMapping
	public long addTag(
		@PathVariable("groupId") long groupId,
		@Valid @RequestBody Name name) {
		long timestamp = System.currentTimeMillis();
		service.addTag(groupId, timestamp, name.getName());
		return timestamp;
	}

	@PostMapping("/change-tags-order")
	public void changeTagsOrder(
		@PathVariable("groupId") long groupId,
		@RequestBody List<NewOrder> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		service.changeTagsOrder(groupId, list);
	}

	@PutMapping("/{tagId}")
	public void renameTag(
		@PathVariable("groupId") long groupId,
		@PathVariable("tagId") long tagId,
		@Valid @RequestBody Name name) {
		service.renameTag(groupId, tagId, name.getName());
	}

	@DeleteMapping("/{tagId}")
	public void deleteTag(
		@PathVariable("groupId") long groupId,
		@PathVariable("tagId") long tagId) {
		service.deleteTag(groupId, tagId);
		expenseCoordinator.deleteTag(groupId, tagId);
	}

}

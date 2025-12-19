package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.annotations.GroupMemberOnly;
import com.csetutorials.expensecircle.beans.Name;
import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupTag;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.GroupTagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/tags")
@GroupMemberOnly
public class GroupTagsController {

	@Autowired
	private GroupTagService groupTagService;
	@Autowired
	private AsyncCalls asyncCalls;

	@GetMapping
	public List<GroupTag> getTags(@PathVariable("groupId") long groupId) {
		List<GroupTag> list = groupTagService.getTags(groupId);
		list.sort(Comparator.comparingLong(GroupTag::getOrderNumber));
		return list;
	}

	@PostMapping
	public long addTag(
		@PathVariable("groupId") long groupId,
		@Valid @RequestBody Name name) {
		long timestamp = System.currentTimeMillis();
		groupTagService.addTag(groupId, timestamp, name.getName());
		return timestamp;
	}

	@PostMapping("/change-tags-order")
	public void changeTagsOrder(
		@PathVariable("groupId") long groupId,
		@RequestBody List<NewOrder> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		groupTagService.changeTagsOrder(groupId, list);
	}

	@PutMapping("/{tagId}")
	public void renameTag(
		@PathVariable("groupId") long groupId,
		@PathVariable("tagId") long tagId,
		@Valid @RequestBody Name name) {
		groupTagService.renameTag(groupId, tagId, name.getName());
	}

	@DeleteMapping("/{tagId}")
	public void deleteTag(
		@PathVariable("groupId") long groupId,
		@PathVariable("tagId") long tagId) {
		asyncCalls.deleteTag(groupId, tagId);
	}

}

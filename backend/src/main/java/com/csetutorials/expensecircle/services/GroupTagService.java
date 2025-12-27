package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.dto.ExpenseTagDto;
import com.csetutorials.expensecircle.entities.GroupTag;
import com.csetutorials.expensecircle.repositories.GroupTagRepository;
import com.csetutorials.expensecircle.utilities.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GroupTagService {

	@Autowired
	private GroupTagRepository repo;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private IdGenerator idGenerator;

	private static final String ORDER_CHANGE_QUERY =
		"UPDATE groupTags SET orderNumber = ? WHERE tagId = ? and groupId = ?";

	public List<GroupTag> getTags(String groupId) {
		return repo.findAllByGroupId(groupId);
	}

	public String addTag(String groupId, String tagName) {
		String id = idGenerator.getStringId();
		repo.save(new GroupTag(groupId, id, tagName, idGenerator.getId()));
		return id;
	}

	public List<GroupTag> addTags(String groupId, Collection<String> tagNames) {
		List<GroupTag> list = new ArrayList<>();
		for (String tagName: tagNames) {
			list.add(new GroupTag(groupId, idGenerator.getStringId(), tagName, idGenerator.getId()));
		}
		repo.saveAll(list);
		return list;
	}

	public void renameTag(String groupId, String tagId, String newTagName) {
		repo.renameTag(groupId, tagId, newTagName);
	}

	public void changeTagsOrder(String groupId, List<NewOrder> list) {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement ps = connection.prepareStatement(ORDER_CHANGE_QUERY)) {
			for (NewOrder order : list) {
				ps.setLong(1, order.getOrderNumber());
				ps.setString(2, order.getId());
				ps.setString(3, groupId);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Set<String> getTagIdsSet(String groupId) {
		return getTags(groupId).stream().map(GroupTag::getTagId).collect(Collectors.toSet());
	}

	@Transactional
	public List<String> resolveTagIds(String groupId, List<ExpenseTagDto> requestTags) {
		if (requestTags == null || requestTags.isEmpty()) {
			return Collections.emptyList();
		}

		// 1. Fetch all existing tags for this group to validate incoming IDs
		List<GroupTag> existingGroupTags = repo.findAllByGroupId(groupId);
		Set<String> validTagIds = existingGroupTags.stream()
			.map(GroupTag::getTagId)
			.collect(Collectors.toSet());

		// 2. Separate valid IDs from the request
		List<String> providedValidIds = requestTags.stream()
			.map(ExpenseTagDto::getTagId)
			.filter(id -> StringUtils.isNotBlank(id) && validTagIds.contains(id))
			.toList();

		// 3. Extract names for tags that have no ID (New Tags)
		// We filter out names that already exist in this group to avoid duplicates
		Set<String> existingNames = existingGroupTags.stream()
			.map(t -> t.getTagName().toLowerCase())
			.collect(Collectors.toSet());

		List<String> namesToCreate = requestTags.stream()
			.filter(t -> StringUtils.isBlank(t.getTagId()) && StringUtils.isNotBlank(t.getTagName()))
			.map(ExpenseTagDto::getTagName)
			.filter(name -> !existingNames.contains(name.toLowerCase()))
			.distinct()
			.toList();

		// 4. Create new tags and get their IDs
		List<GroupTag> newlyCreatedTags = addTags(groupId, namesToCreate);
		List<String> newIds = newlyCreatedTags.stream().map(GroupTag::getTagId).toList();

		// 5. Combine and return
		return Stream.concat(providedValidIds.stream(), newIds.stream()).toList();
	}

	// This method handles the actual DB insertion of new GroupTag entities
	public List<GroupTag> addTags(String groupId, List<String> names) {
		if (names.isEmpty()) return Collections.emptyList();

		List<GroupTag> toSave = names.stream()
			.map(name -> GroupTag.builder()
				.groupId(groupId)
				.tagId(idGenerator.getStringId())
				.tagName(name)
				.orderNumber(idGenerator.getId())
				.build())
			.toList();
		return repo.saveAll(toSave);
	}
}

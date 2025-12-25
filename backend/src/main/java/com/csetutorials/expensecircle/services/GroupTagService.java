package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupTag;
import com.csetutorials.expensecircle.repositories.GroupTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

	public void verifyTags(String groupId, Set<String> tags) {
		tags.retainAll(getTags(groupId).stream().map(GroupTag::getTagId).toList());
	}
}

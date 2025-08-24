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
import java.util.List;

@Service
public class GroupTagService {

	@Autowired
	private GroupTagRepository repo;
	@Autowired
	private DataSource dataSource;

	private static final String ORDER_CHANGE_QUERY =
		"UPDATE groupTags SET orderNumber = ? WHERE tagId = ? and groupId = ?";

	public List<GroupTag> getTags(long groupId) {
		return repo.findAllByGroupId(groupId);
	}

	public void addTag(long groupId, long tagId, String tagName) {
		repo.save(new GroupTag(groupId, tagId, tagName, tagId));
	}

	public void renameTag(long groupId, long tagId, String newTagName) {
		repo.renameTag(groupId, tagId, newTagName);
	}

	public void deleteTag(long groupId, long tagId) {
		repo.deleteTag(groupId, tagId);
	}

	public void changeTagsOrder(long groupId, List<NewOrder> list) {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement ps = connection.prepareStatement(ORDER_CHANGE_QUERY)) {
			for (NewOrder order : list) {
				ps.setLong(1, order.getOrderNumber());
				ps.setLong(2, order.getId());
				ps.setLong(3, groupId);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

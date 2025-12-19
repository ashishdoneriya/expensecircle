package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.NewOrder;
import com.csetutorials.expensecircle.entities.GroupCategory;
import com.csetutorials.expensecircle.repositories.GroupCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class GroupCategoryService {

	@Autowired
	private GroupCategoryRepository repo;
	@Autowired
	private DataSource dataSource;

	private static final String ORDER_CHANGE_QUERY =
		"UPDATE groupCategories SET orderNumber = ? WHERE categoryId = ? and groupId = ?";

	public List<GroupCategory> getCategories(long groupId) {
		return repo.findAllByGroupId(groupId);
	}

	public void addCategory(long groupId, long categoryId, String categoryName) {
		repo.save(new GroupCategory(groupId, categoryId, categoryName, categoryId));
	}

	public void renameCategory(long groupId, long categoryId, String newCategoryName) {
		repo.renameCategory(groupId, categoryId, newCategoryName);
	}

	public void changeCategoriesOrder(long groupId, List<NewOrder> list) {
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

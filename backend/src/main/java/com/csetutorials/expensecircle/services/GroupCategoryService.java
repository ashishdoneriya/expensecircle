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
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupCategoryService {

	@Autowired
	private GroupCategoryRepository repo;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private IdGenerator idGenerator;

	private static final String ORDER_CHANGE_QUERY =
		"UPDATE groupCategories SET orderNumber = ? WHERE categoryId = ? and groupId = ?";

	public List<GroupCategory> getCategories(String groupId) {
		return repo.findAllByGroupId(groupId);
	}

	public String addCategory(String groupId, String categoryName) {
		String categoryId = idGenerator.getStringId();
		repo.save(new GroupCategory(groupId, categoryId, categoryName, idGenerator.getId()));
		return categoryId;
	}

	public void renameCategory(String groupId, String categoryId, String newCategoryName) {
		repo.renameCategory(groupId, categoryId, newCategoryName);
	}

	public void changeCategoriesOrder(String groupId, List<NewOrder> list) {
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

	public void createDefaultCategories(String groupId) {
		List<String> categories = List.of(
			"Home Supplies", "Outside Food", "Health & Medical", "Clothes",
			"Personal Care", "Transportation", "Education & Learning", "Child Care",
			"Entertainment", "Home Maintenance", "Subscriptions & Recharges",
			"Gadgets", "Social & Occasions", "Insurance & Pension Plans", "Investments", "Loans");

		List<GroupCategory> gcList = new ArrayList<>();
		for (String category: categories) {
			gcList.add(new GroupCategory(groupId, idGenerator.getStringId(), category, idGenerator.getId()));
		}
		repo.saveAll(gcList);
	}
}

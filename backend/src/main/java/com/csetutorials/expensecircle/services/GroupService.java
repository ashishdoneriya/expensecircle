package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.entities.Group;
import com.csetutorials.expensecircle.entities.GroupCategory;
import com.csetutorials.expensecircle.repositories.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService {

	@Autowired
	private GroupRepository repo;
	@Autowired
	private GroupCategoryService groupCategoryService;
	@Autowired
	private IdGenerator idGenerator;

	public Group create(String groupName) {
		Group group = new Group();
		group.setGroupId(idGenerator.getStringId());
		group.setGroupName(groupName);
		repo.save(group);
		groupCategoryService.createDefaultCategories(group.getGroupId());
		return group;
	}

	@Transactional
	public void rename(String groupId, String name) {
		Group group = new Group();
		group.setGroupId(groupId);
		group.setGroupName(name);
		repo.save(group);
	}

	public Optional<Group> get(String groupId) {
		return repo.findByGroupId(groupId);
	}

}

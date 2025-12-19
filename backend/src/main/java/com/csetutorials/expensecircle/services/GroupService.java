package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.entities.Group;
import com.csetutorials.expensecircle.repositories.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService {

	@Autowired
	private GroupRepository repo;

	public Group create(String groupName) {
		Group group = new Group();
		group.setGroupId(System.currentTimeMillis());
		group.setGroupName(groupName);
		repo.save(group);
		return group;
	}

	@Transactional
	public void rename(Long groupId, String name) {
		Group group = new Group();
		group.setGroupId(groupId);
		group.setGroupName(name);
		repo.save(group);
	}

	public Optional<Group> get(Long groupId) {
		return repo.findByGroupId(groupId);
	}

}

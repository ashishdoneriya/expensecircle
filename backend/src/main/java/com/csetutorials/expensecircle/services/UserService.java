package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.entities.User;
import com.csetutorials.expensecircle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	public void save(String userId, String name, String pictureUrl) {
		repo.save(new User(userId, name, pictureUrl));
	}

}

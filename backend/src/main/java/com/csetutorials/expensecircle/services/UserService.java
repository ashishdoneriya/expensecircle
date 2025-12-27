package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.entities.User;
import com.csetutorials.expensecircle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	@Autowired
	private IdGenerator idGenerator;

	public String getOrCreateUserId(String email, String name, String pictureUrl) {
		Optional<User> opt = repo.findByEmail(email);
		if (opt.isPresent()) {
			User user = opt.get();
			Thread.ofVirtual().start(() -> repo.save(new User(user.getUserId(), email, name, pictureUrl)));
			return opt.get().getUserId();
		}
		return create(email, name, pictureUrl);
	}

	public String create(String email, String name, String pictureUrl) {
		String id = idGenerator.getStringId();
		repo.save(new User(id, email, name, pictureUrl));
		return id;
	}

	public Optional<User> get(String userId) {
		return repo.findById(userId);
	}


}

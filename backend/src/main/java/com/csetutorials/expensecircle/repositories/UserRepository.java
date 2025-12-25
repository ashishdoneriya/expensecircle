package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(@Param("email") String email);
}

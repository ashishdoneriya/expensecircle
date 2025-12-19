package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}

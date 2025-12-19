package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.Group;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Modifying
	@Transactional
	@Query("delete from Group f where f.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") long groupId);

	Optional<Group> findByGroupId(@Param("groupId") long groupId);
}

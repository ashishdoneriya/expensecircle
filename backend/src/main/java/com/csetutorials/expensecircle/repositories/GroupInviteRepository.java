package com.csetutorials.expensecircle.repositories;

import com.csetutorials.expensecircle.entities.GroupInvite;
import com.csetutorials.expensecircle.entities.ids.GroupInviteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupInviteRepository extends JpaRepository<GroupInvite, GroupInviteId> {
}

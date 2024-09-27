package com.GASB.main.repository;

import com.GASB.main.model.entity.AdminUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUsersRepo extends JpaRepository<AdminUsers, Long> {

    Optional<AdminUsers> findByEmail(@Param("email") String email);
}

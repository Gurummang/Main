package com.GASB.main.repository;

import com.GASB.main.model.entity.MonitoredUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredUsersRepo extends JpaRepository<MonitoredUsers,Long> {
}

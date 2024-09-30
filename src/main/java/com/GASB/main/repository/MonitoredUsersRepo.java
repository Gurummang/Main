package com.GASB.main.repository;

import com.GASB.main.model.entity.MonitoredUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredUsersRepo extends JpaRepository<MonitoredUsers,Long> {

    @Query("SELECT COUNT(m.id) FROM MonitoredUsers m JOIN m.orgSaaS os WHERE os.org.id = :orgId")
    int getTotalUserCount(@Param("orgId") long orgId);
}

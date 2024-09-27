package com.GASB.main.repository;

import com.GASB.main.model.entity.AlertSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertSettingsRepo extends JpaRepository<AlertSettings, Long> {

    @Query("SELECT COUNT(as.id) FROM AlertSettings as WHERE as.adminUsers.org.id = :orgId")
    int countSettingByOrgId(@Param("orgId") long orgId);

}

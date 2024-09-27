package com.GASB.main.repository;

import com.GASB.main.model.entity.AlertSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertSettingsRepo extends JpaRepository<AlertSettings, Long> {

    @Query("SELECT as FROM AlertSettings as WHERE as.adminUsers.org.id = :orgId")
    List<AlertSettings> findAllByOrgId(@Param("orgId") long orgId);

    @Query("SELECT as FROM AlertSettings as " +
        "JOIN as.adminUsers au " +
        "WHERE au.org.id = :orgId AND as.vt = true")
    List<AlertSettings> findAllByOrgIdAndVtTrue(@Param("orgId") long orgId);

    @Query("SELECT as FROM AlertSettings as " +
        "JOIN as.adminUsers au " +
        "WHERE au.org.id = :orgId AND as.suspicious = true")
    List<AlertSettings> findAllByOrgIdAndSuspiciousTrue(@Param("orgId") long orgId);

    @Query("SELECT as FROM AlertSettings as " +
        "JOIN as.adminUsers au " +
        "WHERE au.org.id = :orgId AND as.dlp = true")
    List<AlertSettings> findAllByOrgIdAndDlpTrue(@Param("orgId") long orgId);

}

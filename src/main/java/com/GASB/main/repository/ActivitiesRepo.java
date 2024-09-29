package com.GASB.main.repository;

import com.GASB.main.model.entity.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivitiesRepo extends JpaRepository<Activities, Long> {

    @Query("SELECT a FROM Activities a WHERE a.saasFileId = :saasFileId AND a.eventTs = :timestamp")
    Activities findAllBySaasFileIdAndTimeStamp(@Param("saasFileId") String saasFileId, @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT COUNT(a) FROM Activities a " +
            "LEFT JOIN a.user mu " +
            "LEFT JOIN mu.orgSaaS os " +
            "WHERE os.org.id = :orgId AND os.saas.saasName = :saas AND a.eventType = 'file_upload'")
    long countUploadByOrgAndSaaS(@Param("orgId") long orgId, @Param("saas") String saas);

    @Query("SELECT COUNT(a) FROM Activities a " +
            "LEFT JOIN a.user mu " +
            "LEFT JOIN mu.orgSaaS os " +
            "WHERE os.org.id = :orgId AND os.saas.saasName = :saas AND a.eventType = 'file_change'")
    long countChangeByOrgAndSaaS(@Param("orgId") long orgId, @Param("saas") String saas);

    @Query("SELECT COUNT(a) FROM Activities a " +
            "LEFT JOIN a.user mu " +
            "LEFT JOIN mu.orgSaaS os " +
            "WHERE os.org.id = :orgId AND os.saas.saasName = :saas AND a.eventType = 'file_delete'")
    long countDeleteByOrgAndSaaS(@Param("orgId") long orgId, @Param("saas") String saas);

    @Query("SELECT MAX(a.eventTs) FROM Activities a " +
            "LEFT JOIN a.user mu " +
            "LEFT JOIN mu.orgSaaS os " +
            "WHERE os.org.id = :orgId AND os.saas.saasName = :saas")
    LocalDate findLastActivityByOrgAndSaaS(@Param("orgId") long orgId, @Param("saas") String saas);
}

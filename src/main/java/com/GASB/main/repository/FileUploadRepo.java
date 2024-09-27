package com.GASB.main.repository;

import com.GASB.main.model.entity.FileUpload;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepo extends JpaRepository<FileUpload, Long> {

    @Query("SELECT COUNT(DISTINCT fu) " +
            "FROM FileUpload fu " +
            "JOIN fu.orgSaaS os " +
            "JOIN fu.storedFile sf " +
            "JOIN sf.dlpReport dr " +
            "WHERE fu.deleted = false "+
            "AND sf.id IN ( " +
            "   SELECT DISTINCT d.storedFile.id " +
            "   FROM DlpReport d " +
            "   WHERE d.infoCnt >= 1 " +
            "   AND d.policy.orgSaaS.org.id = :orgId " +
            ") " +
            "AND os.org.id = :orgId")
    int countDlpIssuesByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT COUNT(fu.id) FROM FileUpload fu JOIN OrgSaaS os ON fu.orgSaaS.id = os.id WHERE fu.deleted = false AND os.org.id = :orgId")
    int countFileByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT SUM(sf.size) FROM FileUpload fu " +
            "JOIN StoredFile sf ON fu.hash = sf.saltedHash " +
            "JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
            "WHERE fu.deleted = false AND os.org.id = :orgId")
    int getTotalSizeByOrgId(@Param("orgId") long orgId);
}

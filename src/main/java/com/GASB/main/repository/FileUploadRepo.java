package com.GASB.main.repository;

import com.GASB.main.model.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    @Query("SELECT COUNT(fu.id) " +
            "FROM FileUpload fu " +
            "LEFT JOIN fu.storedFile sf " +
            "LEFT JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
            "WHERE fu.deleted = false AND sf.fileStatus.gscanStatus = 1 AND os.org.id = :orgId")
    int countSuspiciousFileByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT COUNT(fu.id) " +
        "FROM FileUpload fu " +
        "LEFT JOIN fu.storedFile sf " +
        "LEFT JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
        "WHERE fu.deleted = false AND sf.fileStatus.dlpStatus = 1 AND os.org.id = :orgId")
    int countDlpFileByOrgId(@Param("orgId") long orgId);

    @Query("SELECT COUNT(fu.id) " +
            "FROM FileUpload fu " +
            "LEFT JOIN fu.storedFile sf " +
            "LEFT JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
            "WHERE fu.deleted = false AND sf.fileStatus.vtStatus = 1 AND os.org.id = :orgId")
    int countVtFileByOrgId(@Param("orgId") long orgId);

    @Query("SELECT SUM(sf.size) FROM FileUpload fu " +
            "JOIN StoredFile sf ON fu.hash = sf.saltedHash " +
            "JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
            "WHERE fu.deleted = false AND os.org.id = :orgId")
    long getTotalSizeByOrgId(@Param("orgId") long orgId);

    @Query("SELECT COUNT(fu) FROM FileUpload fu " +
            "JOIN fu.orgSaaS os " +
            "WHERE fu.deleted = false AND os.org.id = :orgId AND DATE(fu.timestamp) <= :endDate")
    int getTotalCountUntil(@Param("orgId") long orgId, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(sf.size), 0) FROM FileUpload fu " +
            "JOIN StoredFile sf ON fu.hash = sf.saltedHash " +
            "JOIN fu.orgSaaS os " +
            "WHERE fu.deleted = false AND os.org.id = :orgId AND DATE(fu.timestamp) <= :endDate")
    long getTotalSizeUntil(@Param("orgId") long orgId, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(sf.size), 0) FROM FileUpload fu " +
            "JOIN StoredFile sf ON fu.hash = sf.saltedHash " +
            "JOIN fu.orgSaaS os " +
            "WHERE fu.deleted = false AND os.org.id = :orgId AND os.saas.saasName = :saasName AND DATE(fu.timestamp) <= :endDate")
    long getTotalSizeUntilByOrgAndSaaS(@Param("orgId") long orgId, @Param("saasName") String saasName, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(fu) FROM FileUpload fu " +
            "JOIN fu.orgSaaS os " +
            "WHERE fu.deleted = false AND os.org.id = :orgId AND os.saas.saasName = :saasName AND DATE(fu.timestamp) <= :endDate")
    int getTotalUploadUntilByOrgAndSaaS(@Param("orgId") long orgId, @Param("saasName") String saasName, @Param("endDate") LocalDate endDate);

    @Query("SELECT fu FROM FileUpload fu " +
            "JOIN OrgSaaS os ON fu.orgSaaS.id = os.id " +
            "WHERE fu.deleted = false AND DATE(fu.timestamp) = :date AND os.org.id = :orgId")
    List<FileUpload> findAllByOrgAndDate(@Param("orgId") long orgId, @Param("date") LocalDate date);

    @Query("SELECT fu, dr FROM FileUpload fu LEFT JOIN DlpReport dr ON fu.storedFile.id = dr.storedFile.id WHERE fu.orgSaaS.org.Id = :orgId AND DATE(fu.timestamp) = :date")
    List<Object[]> findFileUploadsWithDlpReports(@Param("orgId") long orgId, @Param("date") LocalDate date);

}

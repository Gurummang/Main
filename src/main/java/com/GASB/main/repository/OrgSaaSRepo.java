package com.GASB.main.repository;

import com.GASB.main.model.entity.OrgSaaS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgSaaSRepo extends JpaRepository<OrgSaaS, Long> {

    @Query("SELECT os FROM OrgSaaS os WHERE os.org.id = :orgId")
    List<OrgSaaS> findAllByOrgId(@Param("orgId") long orgId);

    @Query("SELECT DISTINCT os.saas.saasName FROM OrgSaaS os WHERE os.org.id = :orgId")
    List<String> findDistinctSaaSByOrgId(@Param("orgId") long orgId);

    @Query("SELECT COUNT(os.id) FROM OrgSaaS os WHERE os.org.id = :orgId")
    int countSaaSByOrgId(@Param("orgId") long orgId);
}

package com.GASB.main.repository;

import com.GASB.main.model.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepo extends JpaRepository<Policy, Long> {

    @Query("SELECT p FROM Policy p WHERE p.orgSaaS.org.id = :orgId")
    List<Policy> findAllByOrgId(@Param("orgId") long orgId);

    @Query("SELECT COUNT(p) FROM Policy p WHERE p.orgSaaS.org.id = :orgId")
    int countPolicyByOrgId(@Param("orgId")long orgId);
}

package com.GASB.main.repository;

import com.GASB.main.model.entity.DlpStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DlpStatRepo extends JpaRepository<DlpStat, Long> {
}

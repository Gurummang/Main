package com.GASB.main.repository;

import com.GASB.main.model.entity.Gscan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GscanRepo extends JpaRepository<Gscan, Long> {
}

package com.GASB.main.repository;

import com.GASB.main.model.entity.TypeScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeScanRepo extends JpaRepository<TypeScan, Long> {
}

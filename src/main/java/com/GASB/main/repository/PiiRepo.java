package com.GASB.main.repository;

import com.GASB.main.model.entity.Pii;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiiRepo extends JpaRepository<Pii, Long> {
}

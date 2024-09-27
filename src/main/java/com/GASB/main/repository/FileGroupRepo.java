package com.GASB.main.repository;

import com.GASB.main.model.entity.FileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileGroupRepo extends JpaRepository<FileGroup, Long> {
}

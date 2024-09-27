package com.GASB.main.repository;

import com.GASB.main.model.entity.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStatusRepo extends JpaRepository<FileStatus, Long> {
}

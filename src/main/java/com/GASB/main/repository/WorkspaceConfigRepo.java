package com.GASB.main.repository;

import com.GASB.main.model.entity.WorkspaceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceConfigRepo extends JpaRepository<WorkspaceConfig, Long> {
}

package com.GASB.main.repository;

import com.GASB.main.model.entity.ChannelList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelListRepo extends JpaRepository<ChannelList, Long> {
}

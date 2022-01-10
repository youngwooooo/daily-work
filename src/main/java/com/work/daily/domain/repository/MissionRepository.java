package com.work.daily.domain.repository;

import com.work.daily.domain.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

}

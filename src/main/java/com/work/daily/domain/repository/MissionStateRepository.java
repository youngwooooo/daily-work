package com.work.daily.domain.repository;

import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.entity.MissionStatePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionStateRepository extends JpaRepository<MissionState, MissionStatePK> {
}
package com.work.daily.domain.repository;

import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.pk.MissionStatePK;
import com.work.daily.domain.repository.custom.MissionStateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionStateRepository extends JpaRepository<MissionState, MissionStatePK>, MissionStateRepositoryCustom {
}

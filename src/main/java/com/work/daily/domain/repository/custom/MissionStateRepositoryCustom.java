package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.MissionState;

import java.util.List;

public interface MissionStateRepositoryCustom {
    List<MissionState> findAllMissionStateByMissionSeq(long missionSeq);
}

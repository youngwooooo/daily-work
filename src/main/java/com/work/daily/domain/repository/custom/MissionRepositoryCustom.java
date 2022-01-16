package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL을 사용하기 위해 쿼리를 정의하는 interface
 */
public interface MissionRepositoryCustom {
    List<Mission> findAllMission();
    Optional<Mission> findMission(long missionSeq);
}

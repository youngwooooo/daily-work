package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.MissionState;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionStateRepositoryCustom {
    List<MissionState> findAllMissionStateByMissionSeq(long missionSeq);
    List<MissionState> findMissionStateByMissionSeqAndUserId(long missionSeq, String userId);
    Page<ResponseMissionStateDto> findAllMyMissionState(String userId, Pageable pageable, String search);
    List<MissionState> findAllMissionStateForClose(long missionSeq, LocalDateTime now);
}

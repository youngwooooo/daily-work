package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.MissionState;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MissionStateRepositoryCustom {
    List<MissionState> findAllMissionStateByMissionSeq(long missionSeq);
    List<MissionState> findMissionStateByMissionSeqAndUserId(long missionSeq, String userId);

    // 마이페이지 - 나의 미션 현황 전체 조회
    Page<ResponseMissionStateDto> findAllMyMissionState(String userId, Pageable pageable, String search);
}

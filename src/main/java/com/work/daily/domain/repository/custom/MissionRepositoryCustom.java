package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * QueryDSL을 사용하기 위해 쿼리를 정의하는 interface
 */
public interface MissionRepositoryCustom {
    // 전체 미션 조회
    Page<Mission> findAllMission(Pageable pageable, String search);
    // 미션 단건 조회
    Optional<Mission> findMission(long missionSeq);

    // 마이페이지 - 최근 참여 미션 4건 조회
    List<Mission> findLatelyParticipationMission(String userId);
    // 마이페이지 - 나의 참여 미션 전체 조회
    Page<Mission> findAllLatelyParticipationMission(String userId, Pageable pageable, String search);

    // 마이페이지 - 최근 작성 미션 4건 조회
    List<Mission> findLatelyCreatedMission(String userId);
    // 마이페이지 - 나의 작성 미션 전체 조회
    Page<Mission> findAllLatelyCreatedMission(String userId, Pageable pageable, String search);

    List<Mission> findAllMissionForClose(LocalDateTime now);
}

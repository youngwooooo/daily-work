package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * QueryDSL을 사용하기 위해 쿼리를 정의하는 interface
 */
public interface MissionRepositoryCustom {
    Page<Mission> findAllMission(Pageable pageable);
    Optional<Mission> findMission(long missionSeq);
}

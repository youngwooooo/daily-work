package com.work.daily.domain.repository;

import com.work.daily.domain.entity.Mission;

import java.util.List;

/**
 * QueryDSL을 사용하기 위해 쿼리를 정의하는 interface
 */
public interface MissionRepositoryCustom {
    List<Mission> findAllMission();
}

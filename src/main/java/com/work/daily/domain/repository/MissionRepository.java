package com.work.daily.domain.repository;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.repository.custom.MissionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {

}

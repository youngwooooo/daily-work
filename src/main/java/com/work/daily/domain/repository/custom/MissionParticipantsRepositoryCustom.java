package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.MissionParticipants;

import java.util.Optional;

public interface MissionParticipantsRepositoryCustom {
    Optional<MissionParticipants> findOneMissionParticipant(long missionSeq, long userSeq, String userId);
}

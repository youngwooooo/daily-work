package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.MissionParticipants;

import java.util.List;
import java.util.Optional;

public interface MissionParticipantsRepositoryCustom {
    Optional<MissionParticipants> findOneMissionParticipant(long missionSeq, long userSeq, String userId);
    List<MissionParticipants> findAllMissionParticipantByMissionSeq(long missionSeq);
}

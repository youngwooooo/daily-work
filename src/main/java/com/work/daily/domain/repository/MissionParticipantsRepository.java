package com.work.daily.domain.repository;

import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.pk.MissionParticipantsPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionParticipantsRepository extends JpaRepository<MissionParticipants, MissionParticipantsPK> {
}

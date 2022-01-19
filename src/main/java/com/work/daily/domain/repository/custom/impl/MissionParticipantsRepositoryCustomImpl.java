package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.QMission;
import com.work.daily.domain.entity.QMissionParticipants;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.MissionParticipantsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MissionParticipantsRepositoryCustomImpl implements MissionParticipantsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMissionParticipants qMissionParticipants = QMissionParticipants.missionParticipants;
    QMission qMission = QMission.mission;
    QUser qUser = QUser.user;

    /**
     * 미션 참여자 단건 조회
     * @description 해당 미션에 참여한 미션 참여자 단건 조회
     * @param missionSeq
     * @param userSeq
     * @param userId
     * @return
     */
    @Override
    public Optional<MissionParticipants> findOneMissionParticipant(long missionSeq, long userSeq, String userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMissionParticipants)
                .innerJoin(qMissionParticipants.mission, qMission)
                .fetchJoin()
                .innerJoin(qMissionParticipants.user, qUser)
                .where(qMissionParticipants.missionSeq.eq(missionSeq)
                        .and(qMissionParticipants.userSeq.eq(userSeq))
                        .and(qMissionParticipants.userId.eq(userId)))
                .fetchOne());
    }
}

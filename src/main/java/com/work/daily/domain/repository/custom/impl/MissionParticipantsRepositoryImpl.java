package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.QMission;
import com.work.daily.domain.entity.QMissionParticipants;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.MissionParticipantsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MissionParticipantsRepositoryImpl implements MissionParticipantsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMissionParticipants qMissionParticipants = QMissionParticipants.missionParticipants;
    QMission qMission = QMission.mission;
    QUser qUser = QUser.user;

    /**
     * 모든 미션 참여자 조회
     * @description 미션번호에 따른 해당 미션의 모든 미션 참여자 조회
     * @param missionSeq
     * @return
     */
    @Override
    public List<MissionParticipants> findAllMissionParticipantByMissionSeq(long missionSeq) {
        return jpaQueryFactory
                .selectFrom(qMissionParticipants)
                .innerJoin(qMissionParticipants.user, qUser)
                .fetchJoin()
                .where(qMissionParticipants.missionSeq.eq(missionSeq))
                .fetch();
    }

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
                .fetchJoin()
                .where(qMissionParticipants.missionSeq.eq(missionSeq)
                        .and(qMissionParticipants.userSeq.eq(userSeq))
                        .and(qMissionParticipants.userId.eq(userId)))
                .fetchOne());
    }

}

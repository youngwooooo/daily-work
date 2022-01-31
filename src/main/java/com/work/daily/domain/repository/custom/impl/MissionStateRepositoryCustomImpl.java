package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.entity.QMissionParticipants;
import com.work.daily.domain.entity.QMissionState;
import com.work.daily.domain.repository.custom.MissionStateRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MissionStateRepositoryCustomImpl implements MissionStateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMissionState qMissionState = QMissionState.missionState;
    QMissionParticipants qMissionParticipants = QMissionParticipants.missionParticipants;

    /**
     * 모든 미션 현황 조회
     * @param missionSeq
     * @return
     */
    @Override
    public List<MissionState> findAllMissionStateByMissionSeq(long missionSeq) {
        return jpaQueryFactory.selectFrom(qMissionState)
                .innerJoin(qMissionState.missionParticipants, qMissionParticipants)
                .fetchJoin()
                .where(qMissionState.missionParticipants.missionSeq.eq(missionSeq))
                .fetch();
    }

    /**
     * 나의 제출 미션 조회
     * @param missionSeq
     * @param userId
     * @return
     */
    @Override
    public List<MissionState> findMissionStateByMissionSeqAndUserId(long missionSeq, String userId) {
        return jpaQueryFactory.selectFrom(qMissionState)
                .innerJoin(qMissionState.missionParticipants, qMissionParticipants)
                .fetchJoin()
                .where(qMissionState.missionParticipants.missionSeq.eq(missionSeq).and(qMissionState.missionParticipants.userId.eq(userId)))
                .fetch();
    }

}

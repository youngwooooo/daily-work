package com.work.daily.domain.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.*;
import com.work.daily.domain.repository.custom.MissionStateRepositoryCustom;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MissionStateRepositoryCustomImpl implements MissionStateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMissionState qMissionState = QMissionState.missionState;
    QMissionParticipants qMissionParticipants = QMissionParticipants.missionParticipants;
    QMission qMission = QMission.mission;

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

    /**
     * 마이페이지 - 나의 미션 현황 전체 조회
     * @param userId
     * @return
     */
    @Override
    public Page<ResponseMissionStateDto> findAllMyMissionState(String userId, Pageable pageable, String search) {
        List<ResponseMissionStateDto> content = jpaQueryFactory
                .select(Projections.constructor(ResponseMissionStateDto.class, qMissionState, qMission))
                .from(qMissionState, qMission)
                .innerJoin(qMissionState.missionParticipants, qMissionParticipants)
                .where(qMissionState.missionParticipants.missionSeq.eq(qMission.missionSeq)
                        .and(qMissionState.missionParticipants.userId.eq(userId))
                        .and(missionNmLike(search).or(submittedMissionNmLike(search)))
                )
                .orderBy(qMissionState.submittedMissionDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ResponseMissionStateDto> totalCount = jpaQueryFactory
                .select(Projections.constructor(ResponseMissionStateDto.class, qMissionState, qMission))
                .from(qMissionState, qMission)
                .innerJoin(qMissionState.missionParticipants, qMissionParticipants)
                .where(qMissionState.missionParticipants.missionSeq.eq(qMission.missionSeq)
                        .and(qMissionState.missionParticipants.userId.eq(userId))
                        .and(missionNmLike(search).or(submittedMissionNmLike(search)))
                )
                .orderBy(qMissionState.submittedMissionDt.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount.fetch().size());
    }

    private BooleanExpression missionNmLike(String search) {
        return search != null ? qMission.missionNm.contains(search) : null;
    }

    private BooleanExpression submittedMissionNmLike(String search) {
        return search != null ? qMissionState.submittedMissionNm.contains(search) : null;
    }
}

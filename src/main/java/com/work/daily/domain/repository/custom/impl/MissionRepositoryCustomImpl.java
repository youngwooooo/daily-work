package com.work.daily.domain.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.QMission;
import com.work.daily.domain.entity.QMissionParticipants;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.MissionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MissionRepositoryCustom에 정의된 메서드를 Overriding하여 실제로 구현하는 class
 */
@RequiredArgsConstructor
@Repository
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom {

    // QuerydslConfig에서 Bean으로 등록한 JPAQueryFactory 주입
    private final JPAQueryFactory jpaQueryFactory;

    // Mission, User(Entity)의 쿼리 타입 클래스
    QMission qMission = QMission.mission;
    QUser qUser = QUser.user;
    QMissionParticipants qMissionParticipants = QMissionParticipants.missionParticipants;

    /**
     * 전체 Mission 조회
     * @description 1. 공개여부 Y, 삭제여부 N, 임시여부 N인 전체 Mission 조회
     *              2. 페이징 처리
     *              3. 미션명 / 미션작성자 검색
     * @return 전체 Mission List
     */
    @Override
    public Page<Mission> findAllMission(Pageable pageable, String search) {
        List<Mission> content =  jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.releaseYn.eq("Y")
                        .and(qMission.delYn.eq("N"))
                        .and(qMission.temporaryYn.eq("N")))
                .where(missionNmLike(search).or(missionUserNmLike(search)))
                .orderBy(qMission.insDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Mission> totalCount = jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.releaseYn.eq("Y")
                        .and(qMission.delYn.eq("N"))
                        .and(qMission.temporaryYn.eq("N")))
                .where(missionNmLike(search).or(missionUserNmLike(search)))
                .orderBy(qMission.insDtm.desc());

        return PageableExecutionUtils.getPage(content, pageable, ()-> totalCount.fetch().size());
    }

    private BooleanExpression missionNmLike(String search) {
        return search != null ? qMission.missionNm.contains(search) : null;
    }

    private BooleanExpression missionUserNmLike(String search) {
        return search != null ? qMission.user.userNm.contains(search) : null;
    }

    /**
     * 미션번호에 따른 미션 단건 조회
     * @param missionSeq
     * @return
     */
    @Override
    public Optional<Mission> findMission(long missionSeq) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.missionSeq.eq(missionSeq))
                .fetchOne());
    }

    /**
     * 최근 참여한 미션 5건 조회
     * @param userId
     * @return
     */
    @Override
    public List<Mission> findLatelyParticipationMission(String userId) {
        return jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser).fetchJoin()
                .innerJoin(qMission.MissionParticipants, qMissionParticipants).on(qMission.missionSeq.eq(qMissionParticipants.missionSeq))
                .where(qMission.releaseYn.eq("Y")
                        .and(qMission.delYn.eq("N")
                        .and(qMission.temporaryYn.eq("N")
                        .and(qMissionParticipants.userId.eq(userId))))
                )
                .orderBy(qMissionParticipants.missionJoinApprovalDt.desc())
                .limit(5)
                .fetch();
    }

    /**
     * 나의 참여 미션 전체 조회
     * @param userId
     * @return
     */
    @Override
    public Page<Mission> findAllLatelyParticipationMission(String userId, Pageable pageable, String search) {
        List<Mission> content = jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser).fetchJoin()
                .innerJoin(qMission.MissionParticipants, qMissionParticipants).on(qMission.missionSeq.eq(qMissionParticipants.missionSeq))
                .where(qMission.releaseYn.eq("Y")
                        .and(qMission.delYn.eq("N")
                                .and(qMission.temporaryYn.eq("N")
                                        .and(qMissionParticipants.userId.eq(userId))))
                )
                .orderBy(qMissionParticipants.missionJoinApprovalDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Mission> totalCount = jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser).fetchJoin()
                .innerJoin(qMission.MissionParticipants, qMissionParticipants).on(qMission.missionSeq.eq(qMissionParticipants.missionSeq))
                .where(qMission.releaseYn.eq("Y")
                        .and(qMission.delYn.eq("N")
                                .and(qMission.temporaryYn.eq("N")
                                        .and(qMissionParticipants.userId.eq(userId))))
                )
                .orderBy(qMissionParticipants.missionJoinApprovalDt.desc());

        return PageableExecutionUtils.getPage(content, pageable, ()-> totalCount.fetch().size());
    }


    /**
     * 최근 작성한 미션 5건 조회
     * @param userId
     * @return
     */
    @Override
    public List<Mission> findLatelyCreatedMission(String userId) {
        return jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser).fetchJoin()
                .where(qMission.user.userId.eq(userId))
                .orderBy(qMission.insDtm.desc())
                .limit(5)
                .fetch();
    }
}

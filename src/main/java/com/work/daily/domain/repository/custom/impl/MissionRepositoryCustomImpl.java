package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.QMission;
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

    /**
     * 전체 Mission 조회
     * @description 1. 공개여부 Y, 삭제여부 N, 임시여부 N인 전체 Mission 조회
     *              2. 페이징 처리
     * @return 전체 Mission List
     */
    @Override
    public Page<Mission> findAllMission(Pageable pageable) {
        List<Mission> content =  jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.releaseYn.eq("Y").and(qMission.delYn.eq("N")).and(qMission.temporaryYn.eq("N")))
                .orderBy(qMission.insDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Mission> totalCount = jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.releaseYn.eq("Y").and(qMission.delYn.eq("N")).and(qMission.temporaryYn.eq("N")))
                .orderBy(qMission.insDtm.desc());

        return PageableExecutionUtils.getPage(content, pageable, ()-> totalCount.fetch().size());
    }

    @Override
    public Optional<Mission> findMission(long missionSeq) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.missionSeq.eq(missionSeq))
                .fetchOne());
    }
}

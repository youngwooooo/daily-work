package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.*;
import com.work.daily.domain.repository.custom.MissionRepositoryCustom;
import lombok.RequiredArgsConstructor;
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
     * where : 공개여부 Y, 삭제여부 N, 임시여부 N
     * orderBy : 미션 등록일 최신순
     * innerJoin : Mission, User
     * fetchJoin : 지연 로딩(Lazy) 처리를 위함
     * @return 전체 Mission List
     */
    @Override
    public List<Mission> findAllMission() {
        return jpaQueryFactory
                .selectFrom(qMission)
                .innerJoin(qMission.user, qUser)
                .fetchJoin()
                .where(qMission.releaseYn.eq("Y").and(qMission.delYn.eq("N")).and(qMission.temporaryYn.eq("N")))
                .orderBy(qMission.insDtm.desc())
                .fetch();
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

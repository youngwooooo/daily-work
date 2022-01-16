package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.repository.custom.MissionParticipantsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MissionParticipantsRepositoryCustomImpl implements MissionParticipantsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

}

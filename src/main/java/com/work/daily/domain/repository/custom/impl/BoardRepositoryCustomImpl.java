package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.QBoard;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QBoard qBoard = QBoard.board;
    QUser qUser = QUser.user;

    @Override
    public List<Board> findAllBoard() {
        return jpaQueryFactory
                .selectFrom(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                ).fetch();
    }
}

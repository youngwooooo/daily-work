package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.QBoard;
import com.work.daily.domain.entity.QBoardFile;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QBoard qBoard = QBoard.board;
    QBoardFile qBoardFile = QBoardFile.boardFile;
    QUser qUser = QUser.user;

    /**
     * 전체 게시글 조회
     * @return
     */
    @Override
    public List<Board> findAllBoard() {
        return jpaQueryFactory
                .selectFrom(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                ).fetch();
    }

    /**
     * 게시글 단건(상세) 조회
     * @param boardSeq
     * @return
     */
    @Override
    public Optional<Board> findOneBoard(long boardSeq) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .leftJoin(qBoard.boardFileList, qBoardFile).fetchJoin()
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                        .and(qBoard.boardSeq.eq(boardSeq))
                ).fetchOne());
    }


}
package com.work.daily.domain.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.QBoard;
import com.work.daily.domain.entity.QBoardComment;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.BoardCommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardCommentRepositoryImpl implements BoardCommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QBoardComment qBoardComment = QBoardComment.boardComment;
    QBoard qBoard = QBoard.board;
    QUser qUser = QUser.user;


    /**
     * 전체 댓글 조회
     * @param pageable
     * @return
     */
    @Override
    public Page<BoardComment> findAllParentBoardComment(long boardSeq, Pageable pageable) {
        List<BoardComment> content = jpaQueryFactory
                                        .selectFrom(qBoardComment)
                                        .leftJoin(qBoardComment.board, qBoard).fetchJoin()
                                        .leftJoin(qBoardComment.user, qUser).fetchJoin()
                                        .where(qBoardComment.board.boardSeq.eq(boardSeq)
                                                .and(qBoardComment.delYn.eq("N"))
                                                .and(qBoardComment.parentCommentSeq.eq(0L))
                                        )
                                        .orderBy(qBoardComment.insDtm.desc())
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .fetch();

        JPAQuery<BoardComment> totalCount = jpaQueryFactory
                                        .selectFrom(qBoardComment)
                                        .leftJoin(qBoardComment.board, qBoard).fetchJoin()
                                        .leftJoin(qBoardComment.user, qUser).fetchJoin()
                                        .where(qBoardComment.board.boardSeq.eq(boardSeq)
                                                .and(qBoardComment.delYn.eq("N"))
                                                .and(qBoardComment.parentCommentSeq.eq(0L))
                                        )
                                        .orderBy(qBoardComment.insDtm.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount.fetch().size());
    }

    /**
     * 전체 답글 조회
     * @return
     */
    @Override
    public List<BoardComment> findAllChildBoardComment(long boardSeq) {
        return jpaQueryFactory
                .selectFrom(qBoardComment)
                .leftJoin(qBoardComment.board, qBoard).fetchJoin()
                .leftJoin(qBoardComment.user, qUser).fetchJoin()
                .where(qBoardComment.board.boardSeq.eq(boardSeq)
                        .and(qBoardComment.delYn.eq("N"))
                        .and(qBoardComment.parentCommentSeq.gt(0))
                )
                .orderBy(qBoardComment.insDtm.asc())
                .fetch();
    }
}

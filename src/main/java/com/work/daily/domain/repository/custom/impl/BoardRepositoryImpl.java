package com.work.daily.domain.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.domain.BoardType;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.QBoard;
import com.work.daily.domain.entity.QBoardFile;
import com.work.daily.domain.entity.QUser;
import com.work.daily.domain.repository.custom.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QBoard qBoard = QBoard.board;
    QBoardFile qBoardFile = QBoardFile.boardFile;
    QUser qUser = QUser.user;

    /**
     * 전체 게시글 조회
     * @return
     */
    @Override
    public Page<Board> findAllBoard(Pageable pageable, String search, String category) {
        List<Board> content = jpaQueryFactory
                .selectFrom(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                        .and(boardTypeEq(category))
                        .and(boardNmLike(search).or(boardUserLike(search)))
                )
                .orderBy(qBoard.insDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> totalCount = jpaQueryFactory
                .selectFrom(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                        .and(boardTypeEq(category))
                        .and(boardNmLike(search).or(boardUserLike(search)))
                )
                .orderBy(qBoard.insDtm.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount.fetch().size());
    }

    private BooleanExpression boardNmLike(String search){
        return search != null ? qBoard.boardNm.contains(search) : null;
    }

    private BooleanExpression boardUserLike(String search){
        return search != null ? qBoard.user.userNm.contains(search) : null;
    }

    private BooleanExpression boardTypeEq(String category){
        log.info("boardTypeEq :: 카테고리 : " + category);

        if(category != null){
            if(BoardType.NORMAL.getValue().equals(category)){
                return qBoard.boardType.eq(BoardType.NORMAL);

            }else if(BoardType.QA.getValue().equals(category)){
                return qBoard.boardType.eq(BoardType.QA);

            }else if(BoardType.PR.getValue().equals(category)){
                return qBoard.boardType.eq(BoardType.QA);

            }else {
                return null;
            }
        }

        return null;
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

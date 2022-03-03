package com.work.daily.domain.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.domain.entity.*;
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
    QBoardType qBoardType = QBoardType.boardType;

    /**
     * 전체 게시글 조회
     * @return
     */
    @Override
    public Page<ResponseBoardDto> findAllBoard(Pageable pageable, String search, String category) {
        List<ResponseBoardDto> content = jpaQueryFactory
                .select(Projections.constructor(ResponseBoardDto.class, qBoard, qBoardType.boardTypeNm))
                .from(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .join(qBoardType).on(qBoard.boardType.eq(qBoardType.boardTypeCode))
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.temporaryYn.eq("N"))
                        .and(boardTypeEq(category))
                        .and(boardNmLike(search).or(boardUserLike(search)))
                )
                .orderBy(qBoard.insDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ResponseBoardDto> totalCount = jpaQueryFactory
                .select(Projections.constructor(ResponseBoardDto.class, qBoard, qBoardType.boardTypeNm))
                .from(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .join(qBoardType).on(qBoard.boardType.eq(qBoardType.boardTypeCode))
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
        if(category != null && !category.isEmpty()){
            return qBoard.boardType.eq(category);
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
                        .and(qBoard.boardSeq.eq(boardSeq))
                ).fetchOne());
    }

    /**
     * 마이페이지 - 최근 작성한 게시글 10건 조회
     * @param userId
     * @return
     */
    @Override
    public List<ResponseBoardDto> findBoardCountTen(String userId) {
        return jpaQueryFactory
                .select(Projections.constructor(ResponseBoardDto.class, qBoard, qBoardType.boardTypeNm))
                .from(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .join(qBoardType).on(qBoard.boardType.eq(qBoardType.boardTypeCode))
                .where(qBoard.user.userId.eq(userId)
                    .and(qBoard.delYn.eq("N"))
                )
                .orderBy(qBoard.insDtm.desc())
                .limit(10)
                .fetch();
    }

    /**
     * 마이페이지 - 나의 게시글 - 나의 게시글 전체 조회
     * @param pageable
     * @param search
     * @param category
     * @param userId
     * @return
     */
    @Override
    public Page<ResponseBoardDto> findAllMyBoard(Pageable pageable, String search, String category, String userId) {
        List<ResponseBoardDto> content = jpaQueryFactory
                .select(Projections.constructor(ResponseBoardDto.class, qBoard, qBoardType.boardTypeNm))
                .from(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .join(qBoardType).on(qBoard.boardType.eq(qBoardType.boardTypeCode))
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.user.userId.eq(userId))
                        .and(boardTypeEq(category))
                        .and(boardNmLike(search).or(boardUserLike(search)))
                )
                .orderBy(qBoard.insDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ResponseBoardDto> totalCount = jpaQueryFactory
                .select(Projections.constructor(ResponseBoardDto.class, qBoard, qBoardType.boardTypeNm))
                .from(qBoard)
                .innerJoin(qBoard.user, qUser).fetchJoin()
                .join(qBoardType).on(qBoard.boardType.eq(qBoardType.boardTypeCode))
                .where(qBoard.delYn.eq("N")
                        .and(qBoard.user.userId.eq(userId))
                        .and(boardTypeEq(category))
                        .and(boardNmLike(search).or(boardUserLike(search)))
                )
                .orderBy(qBoard.insDtm.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount.fetch().size());
    }


}

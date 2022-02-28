package com.work.daily.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.domain.BoardType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 게시판 정보
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_BOARD_INFO")
public class Board extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_INCREASE")
    @Column(columnDefinition = "varchar(20) comment '게시글번호'")
    private long boardSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "USER_SEQ"),
            @JoinColumn(name = "USER_ID")
    })
    private User user;

    @Column(columnDefinition = "varchar(60) comment '게시판제목'")
    private String boardNm;

    @Column(columnDefinition = "varchar(300) comment '게시판내용'")
    private String boardDesc;

    @Column(columnDefinition = "varchar(10) comment '삭제여부'")
    private String delYn;

    @Column(columnDefinition = "varchar(10) comment '임시여부'")
    private String temporaryYn;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) comment '게시글구분'")
    private BoardType boardType;

    @Column(columnDefinition = "number comment '조회수'")
    private long viewCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board")
    @JsonManagedReference
    private List<BoardFile> boardFileList;

    // 조회수 증가
    public void increaseViewCount(){
        this.viewCount++;
    }

    // 게시글 삭제여부 N으로 변경
    public void deleteBoard(String delYn){
        this.delYn = delYn;
    }

    // 게시글 수정
    public void modifyBoard(RequestBoardDto requestBoardDto){
        this.boardNm = requestBoardDto.getBoardNm();
        this.boardDesc = requestBoardDto.getBoardDesc();
        this.boardType = requestBoardDto.getBoardType();
    }
}

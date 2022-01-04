package com.work.daily.domain.entity;

import lombok.*;

import javax.persistence.*;

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
    @Column(columnDefinition = "varchar(20) comment '게시글번호'")
    private String boardSeq;

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

    @Column(columnDefinition = "varchar(30) comment '게시글구분'")
    private String boardType;

}

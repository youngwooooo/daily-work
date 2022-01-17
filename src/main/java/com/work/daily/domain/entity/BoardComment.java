package com.work.daily.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 게시판 댓글
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_BOARD_COMMENT")
public class BoardComment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(columnDefinition = "varchar(20) comment '댓글번호'")
    private long commentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "USER_SEQ"),
            @JoinColumn(name = "USER_ID")
    })
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "BOARD_SEQ")
    })
    private Board board;

    @Column(columnDefinition = "varchar(60) comment '부모댓글번호'")
    private long parentCommentSeq;

    @Column(columnDefinition = "varchar(300) comment '댓글내용'")
    private String commentDesc;

    @Column(columnDefinition = "varchar(10) comment '삭제여부'")
    private String delYn;

}

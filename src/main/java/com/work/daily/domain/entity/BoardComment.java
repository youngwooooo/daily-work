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
@SequenceGenerator(
        name = "BOARD_COMMENT_SEQ_INCREASE",
        sequenceName = "BOARD_COMMENT_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
@Entity
@Table(name = "TB_BOARD_COMMENT")
public class BoardComment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_COMMENT_SEQ_INCREASE")
    @Column(columnDefinition = "int comment '댓글번호'")
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

    @Column(columnDefinition = "varchar(600) comment '댓글내용'")
    private String commentDesc;

    @Column(columnDefinition = "varchar(10) comment '삭제여부'")
    private String delYn;

    // 댓글/답글 내용 수정
    public void modifyBoardComment(String commentDesc){
        this.commentDesc = commentDesc;
    }

    // 댓글/답글 삭제(삭제여부 Y로 변경)
    public void deleteBoardComment(String delYn){
        this.delYn = delYn;
    }

}

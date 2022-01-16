package com.work.daily.board.dto;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardCommentDto {
    private Board board;
    private User user;
    private String commentDesc;
    private String delYn;
    private long parentCommentSeq;

    @Builder
    public BoardCommentDto(Board board, User user, String commentDesc, String delYn, long parentCommentSeq) {
        this.board = board;
        this.user = user;
        this.commentDesc = commentDesc;
        this.delYn = delYn;
        this.parentCommentSeq = parentCommentSeq;
    }

    public BoardComment toEntity(){
        return BoardComment.builder()
                .board(board)
                .user(user)
                .commentDesc(commentDesc)
                .delYn(delYn)
                .parentCommentSeq(parentCommentSeq)
                .build();
    }

}

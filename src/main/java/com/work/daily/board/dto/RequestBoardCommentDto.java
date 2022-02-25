package com.work.daily.board.dto;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestBoardCommentDto {

    private Board board;
    private User user;
    private String commentDesc;
    private String delYn;
    private long parentCommentSeq;

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

package com.work.daily.board.dto;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBoardCommentDto {

    private long commentSeq;
    private long boardSeq;
    private long userSeq;
    private String userId;
    private String userNm;
    private String profileImage;
    private String commentDesc;
    private String delYn;
    private long parentCommentSeq;
    private LocalDateTime insDtm;
    private LocalDateTime updDtm;

    public ResponseBoardCommentDto(BoardComment boardComment){
        this.commentSeq = boardComment.getCommentSeq();
        this.boardSeq = boardComment.getBoard().getBoardSeq();
        this.userSeq = boardComment.getUser().getUserSeq();
        this.userId = boardComment.getUser().getUserId();
        this.userNm = boardComment.getUser().getUserNm();
        this.profileImage = boardComment.getUser().getProfileImage();
        this.commentDesc = boardComment.getCommentDesc();
        this.delYn = boardComment.getDelYn();
        this.parentCommentSeq = boardComment.getParentCommentSeq();
        this.insDtm = boardComment.getInsDtm();
        this.updDtm = boardComment.getUpdDtm();
    }
}

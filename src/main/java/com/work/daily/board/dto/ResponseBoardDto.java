package com.work.daily.board.dto;

import com.work.daily.domain.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseBoardDto {

    private long boardSeq;
    private long userSeq;
    private String userId;
    private String userNm;
    private String profileImage;
    private LocalDateTime insDtm;
    private String boardNm;
    private String boardDesc;
    private String delYn;
    private String temporaryYn;
    private String boardType;

    public ResponseBoardDto(Board board){
        this.boardSeq = board.getBoardSeq();
        this.userSeq = board.getUser().getUserSeq();
        this.userId = board.getUser().getUserId();
        this.userNm = board.getUser().getUserNm();
        this.profileImage = board.getUser().getProfileImage();
        this.insDtm = board.getInsDtm();
        this.boardNm = board.getBoardNm();
        this.boardDesc = board.getBoardDesc();
        this.delYn = board.getDelYn();
        this.temporaryYn = board.getTemporaryYn();
        this.boardType = board.getBoardType();
    }
}

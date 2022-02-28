package com.work.daily.board.dto;

import com.work.daily.domain.BoardType;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardFile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private BoardType boardType;
    private long viewCount;
    private List<BoardFile> boardFileList;

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
        this.viewCount = board.getViewCount();
    }
}

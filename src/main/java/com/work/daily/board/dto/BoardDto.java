package com.work.daily.board.dto;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private User user;
    private String boardNm;
    private String boardDesc;
    private String delYn;
    private String temporaryYn;
    private String boardType;

    @Builder
    public BoardDto(User user, String boardNm, String boardDesc, String delYn, String temporaryYn, String boardType) {
        this.user = user;
        this.boardNm = boardNm;
        this.boardDesc = boardDesc;
        this.delYn = delYn;
        this.temporaryYn = temporaryYn;
        this.boardType = boardType;
    }

    public Board toEntity(){
        return Board.builder()
                .user(user)
                .boardNm(boardNm)
                .boardDesc(boardDesc)
                .delYn(delYn)
                .temporaryYn(temporaryYn)
                .boardType(boardType)
                .build();
    }

}

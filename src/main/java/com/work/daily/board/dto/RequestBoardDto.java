package com.work.daily.board.dto;

import com.work.daily.domain.BoardType;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestBoardDto {

    private User user;
    private String boardNm;
    private String boardDesc;
    private String delYn;
    private String temporaryYn;
    private BoardType boardType;

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

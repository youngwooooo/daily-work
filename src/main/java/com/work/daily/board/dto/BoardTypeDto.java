package com.work.daily.board.dto;

import com.work.daily.domain.entity.BoardType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardTypeDto {

    private long boardTypeSeq;
    private String boardTypeCode;
    private String boardTypeNm;

    public BoardTypeDto(BoardType boardType) {
        this.boardTypeSeq = boardType.getBoardTypeSeq();
        this.boardTypeCode = boardType.getBoardTypeCode();
        this.boardTypeNm = boardType.getBoardTypeNm();
    }
}

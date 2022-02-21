package com.work.daily.boardfile.dto;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardFile;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFileDto {

    private long fileSeq;
    private long fileOrder;
    private Board board;
    private String fileOriginNm;
    private String fileServerNm;
    private String fileUploadPath;
    private long fileSize;
    private String imageYn;
    private LocalDateTime insDtm;

    public BoardFile toEntity(){
        return BoardFile.builder()
                .fileOrder(fileOrder)
                .board(board)
                .fileOriginNm(fileOriginNm)
                .fileServerNm(fileServerNm)
                .fileUploadPath(fileUploadPath)
                .fileSize(fileSize)
                .imageYn(imageYn)
                .insDtm(insDtm)
                .build();
    }

}

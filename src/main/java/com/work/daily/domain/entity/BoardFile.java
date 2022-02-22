package com.work.daily.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_BOARD_FILE")
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_FILE_SEQ_INCREASE")
    @Column(columnDefinition = "varchar(20) comment '게시글파일번호'")
    private long fileSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_SEQ")
    @JsonBackReference
    private Board board;

    @Column(columnDefinition = "varchar(300) comment '게시글파일원본파일명'")
    private String fileOriginNm;

    @Column(columnDefinition = "varchar(300) comment '게시글파일서버파일명'")
    private String fileServerNm;

    @Column(columnDefinition = "varchar(200) comment '게시글파일경로'")
    private String fileUploadPath;

    @Column(columnDefinition = "varchar(100) comment '게시글파일크기'")
    private long fileSize;

    @Column(columnDefinition = "varchar(5) comment '이미지여부'")
    private String imageYn;

    @Column(columnDefinition = "varchar(30) comment '게시글파일등록일자'")
    private LocalDateTime insDtm;
}

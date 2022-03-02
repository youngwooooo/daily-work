package com.work.daily.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SequenceGenerator(
        name = "BOARD_TYPE_SEQ_INCREASE",
        sequenceName = "BOARD_TYPE_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
@Entity
@Table(name = "TB_BOARD_TYPE")
public class BoardType extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_TYPE_SEQ_INCREASE")
    @Column(columnDefinition = "varchar(20) comment '게시글분류번호'")
    private long boardTypeSeq;

    @Column(columnDefinition = "varchar(20) comment '게시글분류코드'")
    private String boardTypeCode;

    @Column(columnDefinition = "varchar(20) comment '게시글분류명'")
    private String boardTypeNm;


}

package com.work.daily.domain.pk;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@SequenceGenerator(
        name = "BOARD_SEQ_INCREASE",
        sequenceName = "BOARD_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
public class BoardPk implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_INCREASE")
    private long boardSeq;

    @Id
    private long userSeq;

    @Id
    private String userId;

}

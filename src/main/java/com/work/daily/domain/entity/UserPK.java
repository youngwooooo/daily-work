package com.work.daily.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@SequenceGenerator(
        name = "USER_SEQ_INCREASE",
        sequenceName = "USER_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
@EqualsAndHashCode
public class UserPK implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(30) comment '회원번호'")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_INCREASE")
    private long userSeq;

    @Id
    @Column(columnDefinition = "varchar(60) comment '회원ID'")
    private String userId;

}

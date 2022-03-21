package com.work.daily.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@SequenceGenerator(
        name = "MISSION_STATE_SEQ_INCREASE",
        sequenceName = "MISSION_STATE_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
@AllArgsConstructor
@Builder
public class MissionStatePK implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MISSION_STATE_SEQ_INCREASE")
    @Column(columnDefinition = "int comment '미션현황번호'")
    private long missionStateSeq;

    @Id
    @Column(columnDefinition = "int comment '미션주차'")
    private long missionStateWeek;

}

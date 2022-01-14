package com.work.daily.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class MissionStatePK implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(20) comment '미션현황번호'")
    private long missionStateSeq;

    @Id
    @Column(columnDefinition = "varchar(20) comment '미션주차'")
    private long missionStateWeek;

}

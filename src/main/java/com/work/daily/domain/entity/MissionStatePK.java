package com.work.daily.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class MissionStatePK implements Serializable {

    @Id
    private long missionStateSeq;

    @Id
    private long missionStateWeek;

}

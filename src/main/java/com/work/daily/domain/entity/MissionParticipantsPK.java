package com.work.daily.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class MissionParticipantsPK implements Serializable {

    @Id
    private long missionSeq;

    @Id
    private long userSeq;

    @Id
    private String userId;
}

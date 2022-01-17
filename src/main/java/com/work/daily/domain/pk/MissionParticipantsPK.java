package com.work.daily.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionParticipantsPK implements Serializable {

    @Id
    private long missionSeq;

    @Id
    private long userSeq;

    @Id
    private String userId;
}

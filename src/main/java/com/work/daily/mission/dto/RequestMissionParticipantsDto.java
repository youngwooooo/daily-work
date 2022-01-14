package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionParticipants;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMissionParticipantsDto {

    private long missionSeq;
    private long userSeq;
    private String userId;
    private LocalDateTime missionJoinDt;
    private String missionJoinYn;
    private LocalDateTime missionJoinApprovalDt;

    public MissionParticipants toEntity(){
        return MissionParticipants.builder()
                                    .missionSeq(missionSeq)
                                    .userSeq(userSeq)
                                    .userId(userId)
                                    .missionJoinDt(missionJoinDt)
                                    .missionJoinYn(missionJoinYn)
                                    .missionJoinApprovalDt(missionJoinApprovalDt)
                                    .build();
    }
}

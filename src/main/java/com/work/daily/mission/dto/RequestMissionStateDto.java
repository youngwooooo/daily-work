package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.MissionState;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMissionStateDto {

    private long missionStateSeq;
    private long missionStateWeek;
    private MissionParticipants missionParticipants;
    private String submittedMissionNm;
    private String submittedMissionDesc;
    private String submittedMissionImage;
    private LocalDateTime submittedMissionDt;
    private String approvalYn;
    private LocalDateTime approvalDt;

    public MissionState toEntity(){
        return MissionState.builder()
                            .missionStateWeek(missionStateWeek)
                            .missionParticipants(missionParticipants)
                            .submittedMissionNm(submittedMissionNm)
                            .submittedMissionDesc(submittedMissionDesc)
                            .submittedMissionImage(submittedMissionImage)
                            .submittedMissionDt(submittedMissionDt)
                            .approvalYn(approvalYn)
                            .approvalDt(approvalDt)
                            .build();
    }

}

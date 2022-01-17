package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionParticipants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMissionParticipantsDto {

    @NotNull(message = "미션번호가 존재하지 않습니다.")
    private long missionSeq;
    @NotNull(message = "회원번호가 존재하지 않습니다.")
    private long userSeq;
    @NotBlank(message = "회원ID가 존재하지 않습니다.")
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

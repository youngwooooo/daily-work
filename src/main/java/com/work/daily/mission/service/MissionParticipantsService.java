package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.pk.MissionParticipantsPK;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionParticipantsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionParticipantsService {

    private final MissionParticipantsRepository missionParticipantsRepository;

    /**
     * 모든 미션 참여자 조회
     * @description 미션번호에 해당하는 모든 미션 참여자를 조회
     * @param missionSeq
     * @return
     */
    public List<ResponseMissionParticipantsDto> findAllMissionParticipantByMissionSeq(long missionSeq){
        List<MissionParticipants> findAllMissionParticipant = missionParticipantsRepository.findAllMissionParticipantByMissionSeq(missionSeq);
        return findAllMissionParticipant.stream().map(ResponseMissionParticipantsDto::new).collect(Collectors.toList());
    }

    /**
     * 미션 상세 조회 - [미션 참여하기]
     * @param requestMissionParticipantsDto
     * @return
     */
    @Transactional
    public String joinMission(RequestMissionParticipantsDto requestMissionParticipantsDto){

        MissionParticipantsPK missionParticipantsPK = MissionParticipantsPK.builder()
                .missionSeq(requestMissionParticipantsDto.getMissionSeq())
                .userSeq(requestMissionParticipantsDto.getUserSeq())
                .userId(requestMissionParticipantsDto.getUserId()).build();

        Optional<MissionParticipants> findMissionParticipants = missionParticipantsRepository.findById(missionParticipantsPK);
        if(findMissionParticipants.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        MissionParticipants missionParticipants = missionParticipantsRepository.save(requestMissionParticipantsDto.toEntity());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 미션 상세 조회 - [미션 탈퇴하기]
     * @param requestMissionParticipantsDto
     * @return
     */
    @Transactional
    public String secessionMission(RequestMissionParticipantsDto requestMissionParticipantsDto){

        MissionParticipantsPK missionParticipantsPK = MissionParticipantsPK.builder()
                .missionSeq(requestMissionParticipantsDto.getMissionSeq())
                .userSeq(requestMissionParticipantsDto.getUserSeq())
                .userId(requestMissionParticipantsDto.getUserId()).build();

        Optional<MissionParticipants> findMissionParticipants = missionParticipantsRepository.findById(missionParticipantsPK);
        if(!findMissionParticipants.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        missionParticipantsRepository.delete(requestMissionParticipantsDto.toEntity());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 미션 상세 조회 - [미션 참여자 관리] - [승인]
     * @param requestMissionParticipantsDto
     * @return
     */
    @Transactional
    public ResponseMissionParticipantsDto approveParticipants(RequestMissionParticipantsDto requestMissionParticipantsDto){

        Optional<MissionParticipants> findMissionParticipants = missionParticipantsRepository.findOneMissionParticipant(requestMissionParticipantsDto.getMissionSeq(), requestMissionParticipantsDto.getUserSeq(), requestMissionParticipantsDto.getUserId());
        if(!findMissionParticipants.isPresent()){
            throw new IllegalArgumentException("미션 참여자가 아닙니다. 미션참여자 ID : " + requestMissionParticipantsDto.getUserId());
        }

        findMissionParticipants.get().approveParticipants(requestMissionParticipantsDto);

        ResponseMissionParticipantsDto responseMissionParticipantsDto = ResponseMissionParticipantsDto.builder()
                .missionSeq(findMissionParticipants.get().getMissionSeq())
                .user(findMissionParticipants.get().getUser())
                .build();

        return responseMissionParticipantsDto;
    }

    /**
     * 미션 상세 조회 - [미션 참여자 관리] - [강퇴]
     * @param requestMissionParticipantsDto
     * @return
     */
    @Transactional
    public ResponseMissionParticipantsDto expulsionParticipants(RequestMissionParticipantsDto requestMissionParticipantsDto) {

        Optional<MissionParticipants> findMissionParticipants = missionParticipantsRepository.findOneMissionParticipant(requestMissionParticipantsDto.getMissionSeq(), requestMissionParticipantsDto.getUserSeq(), requestMissionParticipantsDto.getUserId());
        if(!findMissionParticipants.isPresent()){
            throw new IllegalArgumentException("미션 참여자가 아닙니다. 미션참여자 ID : " + requestMissionParticipantsDto.getUserId());
        }

        ResponseMissionParticipantsDto responseMissionParticipantsDto = ResponseMissionParticipantsDto.builder()
                .missionSeq(findMissionParticipants.get().getMissionSeq())
                .user(findMissionParticipants.get().getUser())
                .build();

        missionParticipantsRepository.delete(requestMissionParticipantsDto.toEntity());

        return responseMissionParticipantsDto;
    }

}

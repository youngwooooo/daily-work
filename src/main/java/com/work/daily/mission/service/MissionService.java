package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionParticipantsRepository missionParticipantsRepository;

    @Value("${custom.path.mission-image}")
    private String missionUploadPath;

    /**
     * 전체 Mission 조회
     * @return 전체 Mission List
     */
    @Transactional(readOnly = true)
    public List<ResponseMissionDto> findAllMissions(){
        // 전체 Mission 조회
        List<Mission> findAllMission = missionRepository.findAllMission();
        // Mission -> ResponseMissionDto 타입으로 변경하여 List 생성
        return findAllMission.stream().map(ResponseMissionDto::new).collect(Collectors.toList());
    }

    /**
     * Mission 단건 조회
     * @param missionSeq
     * @return findMissionToDto
     */
    @Transactional(readOnly = true)
    public ResponseMissionDto detailMission(Long missionSeq){
        Optional<Mission> findMission = missionRepository.findMission(missionSeq);
        if(!findMission.isPresent()){
            throw new IllegalArgumentException("해당 미션이 존재하지 않습니다. 미션 번호 : " + missionSeq);
        }

        ResponseMissionDto findMissionToDto = ResponseMissionDto.builder()
                                                .missionSeq(findMission.get().getMissionSeq())
                                                .user(findMission.get().getUser())
                                                .missionNm(findMission.get().getMissionNm())
                                                .missionDesc(findMission.get().getMissionDesc())
                                                .missionStDt(findMission.get().getMissionStDt())
                                                .missionEndDt(findMission.get().getMissionEndDt())
                                                .releaseYn(findMission.get().getReleaseYn())
                                                .autoAccessYn(findMission.get().getAutoAccessYn())
                                                .masterYn(findMission.get().getMasterYn())
                                                .delYn(findMission.get().getDelYn())
                                                .temporaryYn(findMission.get().getTemporaryYn())
                                                .reviewGrade(findMission.get().getReviewGrade())
                                                .missionImage(findMission.get().getMissionImage())
                                                .MissionParticipants(findMission.get().getMissionParticipants())
                                                .build();

        return findMissionToDto;
    }

    /**
     * 미션 만들기 - [등록]
     * @param requestMissionDto
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional
    public String save(RequestMissionDto requestMissionDto, MultipartFile file) throws IOException {
        // 대표 이미지가 존재하지 않을 때
        if(file == null){
            requestMissionDto.setMissionImage("/img/common/basic_mission.jpg");
            Mission savedMission = missionRepository.save(requestMissionDto.toEntity());

            // 미션 참여자 생성(미션 생성자는 미션 참여자에 자동으로 추가됨)
            if(savedMission != null){
                RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                        .missionSeq(savedMission.getMissionSeq())
                        .userSeq(savedMission.getUser().getUserSeq())
                        .userId(savedMission.getUser().getUserId())
                        .missionJoinDt(savedMission.getInsDtm())
                        .missionJoinYn("Y")
                        .missionJoinApprovalDt(savedMission.getInsDtm())
                        .build();

                missionParticipantsRepository.save(requestMissionParticipantsDto.toEntity());
            }

            return ReturnResult.SUCCESS.getValue();
        }

        // 미션 생성
        Mission savedMission = missionRepository.save(requestMissionDto.toEntity());

        // 생성된 미션 번호
        Long missionSeq = savedMission.getMissionSeq();
        log.info("생성 후 조회한 미션 번호 : " + missionSeq);

        // 업로드될 폴더 생성
        File uploadFolder = new File(missionUploadPath + missionSeq);
        if(!uploadFolder.exists()){
            uploadFolder.mkdir();
        }

        // 대표 이미지 이름
        String fileOriginalName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
        // 대표 이미지 생성
        File missionImage = new File(uploadFolder, fileOriginalName);
        file.transferTo(missionImage);

        // DB에 저장할 대표 이미지 경로
        String missionImagePath = "/mission/" + missionSeq + "/" + fileOriginalName;

        // 생성된 미션의 missionImage 값 수정
        savedMission.modifyMissionImage(missionImagePath);

        // 미션 참여자 생성(미션 생성자는 미션 참여자에 자동으로 추가됨)
        if(savedMission != null){
            RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                    .missionSeq(savedMission.getMissionSeq())
                    .userSeq(savedMission.getUser().getUserSeq())
                    .userId(savedMission.getUser().getUserId())
                    .missionJoinDt(savedMission.getInsDtm())
                    .missionJoinYn("Y")
                    .missionJoinApprovalDt(savedMission.getInsDtm())
                    .build();

            missionParticipantsRepository.save(requestMissionParticipantsDto.toEntity());
        }

        return ReturnResult.SUCCESS.getValue();
    }

    @Transactional
    public MissionParticipants joinMission(RequestMissionParticipantsDto requestMissionParticipantsDto){

        Optional<Mission> findMission = missionRepository.findMission(requestMissionParticipantsDto.getMissionSeq());
        if(!findMission.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 미션입니다. 미션번호 : " + requestMissionParticipantsDto.getMissionSeq());
        }

        MissionParticipants missionParticipants = missionParticipantsRepository.save(requestMissionParticipantsDto.toEntity());

        return missionParticipants;
    }
}

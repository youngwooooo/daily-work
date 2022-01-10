package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.mission.dto.RequestMissionDto;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;

    @Value("${custom.path.mission-image}")
    private String missionUploadPath;

    @Transactional
    public List<ResponseMissionDto> findAllMissions(){
        List<Mission> entityMissions = missionRepository.findAll();
        List<ResponseMissionDto> dtoMissions = entityMissions.stream().map(ResponseMissionDto::new).collect(Collectors.toList());
        return null;
    }

    @Transactional
    public String save(RequestMissionDto requestMissionDto, MultipartFile file) throws IOException {
        // 대표 이미지가 존재하지 않을 때
        if(file == null){
            requestMissionDto.setMissionImage("/img/common/basic_mission.jpg");
            missionRepository.save(requestMissionDto.toEntity());
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

        return ReturnResult.SUCCESS.getValue();
    }
}

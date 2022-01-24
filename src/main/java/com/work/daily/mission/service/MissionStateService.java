package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.repository.MissionStateRepository;
import com.work.daily.mission.dto.RequestMissionStateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionStateService {

    @Value("${custom.path.mission-image}")
    private String missionUploadPath;

    @Value("${custom.path.root-image}")
    private String rootImagePath;

    private final MissionStateRepository missionStateRepository;

    @Transactional
    public String save(RequestMissionStateDto requestMissionStateDto, MultipartFile file) throws IOException {

        // 업로드될 폴더 생성
        File uploadFolder = new File(missionUploadPath + requestMissionStateDto.getMissionParticipants().getMissionSeq() + "/" + requestMissionStateDto.getMissionStateWeek() + "/" + getFolder() + "/" + requestMissionStateDto.getMissionParticipants().getUserId());
        if(!uploadFolder.exists()){
            uploadFolder.mkdirs();
        }
        log.info("uploadFolder : " + uploadFolder.toString());

        // 제출 미션 이미지 이름
        String fileOriginalName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
        // 제출 미션 이미지 생성
        File submitMissionImage = new File(uploadFolder, fileOriginalName);
        file.transferTo(submitMissionImage);

        // DB에 저장할 대표 이미지 경로
        String submitMissionImagePath = "/mission/" + requestMissionStateDto.getMissionParticipants().getMissionSeq() + "/" + requestMissionStateDto.getMissionParticipants().getUserId() + "/" + requestMissionStateDto.getMissionStateWeek() + "/" + fileOriginalName;

        // dto에 저장
        requestMissionStateDto.setSubmittedMissionImage(submitMissionImagePath);

        // 제출 미션 생성
        missionStateRepository.save(requestMissionStateDto.toEntity());

        return ReturnResult.SUCCESS.getValue();
    }

    private String getFolder(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String folder = sdf.format(date);
        return folder.replace("-", File.separator);
    }
}

package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.repository.MissionStateRepository;
import com.work.daily.mission.dto.RequestMissionStateDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionStateService {

    @Value("${custom.path.mission-image}")
    private String missionUploadPath;

    @Value("${custom.path.root-image}")
    private String rootImagePath;

    private final MissionStateRepository missionStateRepository;

    /** [미션 제출] - [제출]
     * @description 미션 결과 제출
     * @param requestMissionStateDto
     * @param file
     * @return
     * @throws IOException
     */
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

        // DB에 저장할 제출 미션 이미지 경로
        String submitMissionImagePath = uploadFolder.toString().substring(28).replaceAll("\\\\", "/") +  "/" + fileOriginalName;

        // dto에 저장
        requestMissionStateDto.setSubmittedMissionImage(submitMissionImagePath);

        // 제출 미션 생성
        missionStateRepository.save(requestMissionStateDto.toEntity());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 년/월/일 폴더를 만들어주는 메서드
     * @return
     */
    private String getFolder(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String folder = sdf.format(date);
        return folder.replace("-", File.separator);
    }

    /**
     * 모든 미션 현황 조회
     * @description 미션 번호에 따른 모든 미션 현황을 조회하여 VIEW에 보여주기 위함
     * @param missionSeq
     * @return
     */
    public List<ResponseMissionStateDto> findAllMissionStateByMissionSeq(long missionSeq){
        List<MissionState> findMissionStates = missionStateRepository.findAllMissionStateByMissionSeq(missionSeq);
        return findMissionStates.stream().map(ResponseMissionStateDto::new).collect(Collectors.toList());
    }

    /**
     * 나의 제출 미션 조회
     * @description 미션 번호, 회원 ID에 따른 자신의 모든 제출 미션 조회
     * @param missionSeq
     * @param userId
     * @return
     */
    public List<ResponseMissionStateDto> findMissionStateByMissionSeqAndUserId(long missionSeq, String userId){
        List<MissionState> findMissionStateByMissionSeqAndUserId = missionStateRepository.findMissionStateByMissionSeqAndUserId(missionSeq, userId);
        return findMissionStateByMissionSeqAndUserId.stream().map(ResponseMissionStateDto::new).collect(Collectors.toList());
    }
}

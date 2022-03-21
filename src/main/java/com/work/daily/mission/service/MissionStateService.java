package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.pk.MissionStatePK;
import com.work.daily.domain.repository.MissionStateRepository;
import com.work.daily.mission.dto.RequestMissionStateDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

        if(file == null){
            return  ReturnResult.FAIL.getValue();
        }

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
    @Transactional(readOnly = true)
    public List<ResponseMissionStateDto> findAllMissionStateByMissionSeq(long missionSeq){
        List<MissionState> findMissionStates = missionStateRepository.findAllMissionStateByMissionSeq(missionSeq);
        return findMissionStates.stream().map(ResponseMissionStateDto::new).collect(Collectors.toList());
    }

    /**
     * 미션현황 단건 조회
     * @description 미션현황 단건 조회
     * @param missionStateSeq
     * @param missionStateWeek
     * @return
     */
    @Transactional(readOnly = true)
    public ResponseMissionStateDto findOneMissionState(long missionStateSeq, long missionStateWeek){
        MissionStatePK missionStatePK = MissionStatePK.builder()
                                                .missionStateSeq(missionStateSeq)
                                                .missionStateWeek(missionStateWeek).build();

        Optional<MissionState> findMissionState = missionStateRepository.findById(missionStatePK);
        if(!findMissionState.isPresent()){
            throw new IllegalArgumentException("제출 미션이 존재하지 않습니다. 미션현황번호 : " + missionStateWeek);
        }

        ResponseMissionStateDto responseMissionStateDto = ResponseMissionStateDto.builder()
                .submittedMissionNm(findMissionState.get().getSubmittedMissionNm())
                .submittedMissionDesc(findMissionState.get().getSubmittedMissionDesc())
                .submittedMissionImage(findMissionState.get().getSubmittedMissionImage())
                .approvalYn(findMissionState.get().getApprovalYn())
                .rejectionYn(findMissionState.get().getRejectionYn())
                .rejectionDt(findMissionState.get().getRejectionDt())
                .rejectionDesc(findMissionState.get().getRejectionDesc())
                .build();

        return responseMissionStateDto;
    }

    /**
     * 승인 대기 미션현황 수정
     * @description 미션현황의 승인여부를 Y로 수정한다.
     * @param requestMissionStateDto
     * @return
     */
    @Transactional
    public String modifyMissionStateApprovalYn(RequestMissionStateDto requestMissionStateDto){
        MissionStatePK missionStatePK = MissionStatePK.builder()
                                                    .missionStateSeq(requestMissionStateDto.getMissionStateSeq())
                                                    .missionStateWeek(requestMissionStateDto.getMissionStateWeek()).build();

        Optional<MissionState> findMissionState =  missionStateRepository.findById(missionStatePK);
        if(!findMissionState.isPresent()){
            throw new IllegalArgumentException("해당 미션 현황이 존재하지 않습니다. 미션현황번호 : " + requestMissionStateDto.getMissionStateSeq());
        }

        findMissionState.get().modifyMissionStateApprovalYn(requestMissionStateDto.getApprovalDt());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 승인 대기 미션현황 수정
     * @description 미션현황의 반려여부 N -> Y, 반려 일자, 반려 내용을 수정한다.
     * @param requestMissionStateDto
     * @return
     */
    @Transactional
    public String modifyMissionStateRejectionInfo(RequestMissionStateDto requestMissionStateDto) {
        MissionStatePK missionStatePK = MissionStatePK.builder()
                .missionStateSeq(requestMissionStateDto.getMissionStateSeq())
                .missionStateWeek(requestMissionStateDto.getMissionStateWeek()).build();

        Optional<MissionState> findMissionState =  missionStateRepository.findById(missionStatePK);
        if(!findMissionState.isPresent()){
            throw new IllegalArgumentException("해당 미션 현황이 존재하지 않습니다. 미션현황번호 : " + requestMissionStateDto.getMissionStateSeq());
        }

        findMissionState.get().modifyMissionStateRejectionInfo(requestMissionStateDto.getRejectionYn(), requestMissionStateDto.getRejectionDt(), requestMissionStateDto.getRejectionDesc());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 나의 제출 미션현황 전체 조회
     * @description 미션 번호, 회원 ID에 따른 자신의 모든 제출 미션 조회
     * @param missionSeq
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseMissionStateDto> findMissionStateByMissionSeqAndUserId(long missionSeq, String userId){
        List<MissionState> findMissionStateByMissionSeqAndUserId = missionStateRepository.findMissionStateByMissionSeqAndUserId(missionSeq, userId);
        return findMissionStateByMissionSeqAndUserId.stream().map(ResponseMissionStateDto::new).collect(Collectors.toList());
    }

    /**
     * 나의 제출 미션현황 수정
     * @description 나의 제출 미션 제목, 내용, 이미지, 반려 여부 N, 반려 일자 = null, 반려 내용 = null로 수정한다.
     * @param requestMissionStateDto
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional
    public String modifyMyMissionState(RequestMissionStateDto requestMissionStateDto, MultipartFile file) throws IOException {
        MissionStatePK missionStatePK = MissionStatePK.builder()
                                                .missionStateSeq(requestMissionStateDto.getMissionStateSeq())
                                                .missionStateWeek(requestMissionStateDto.getMissionStateWeek()).build();

        Optional<MissionState> findMissionState = missionStateRepository.findById(missionStatePK);
        if(!findMissionState.isPresent()){
            throw new IllegalArgumentException("제출 미션이 존재하지 않습니다. 미션현황번호 : " + requestMissionStateDto.getMissionStateSeq());
        }

        if(file == null){
            findMissionState.get().modifyMyMissionState(requestMissionStateDto.getSubmittedMissionNm(), requestMissionStateDto.getSubmittedMissionDesc(), findMissionState.get().getSubmittedMissionImage(), requestMissionStateDto.getRejectionYn(), requestMissionStateDto.getRejectionDt(), requestMissionStateDto.getRejectionDesc());
        }else {
            // 파일 관련
            File uploadFolder = new File(rootImagePath + findMissionState.get().getSubmittedMissionImage().substring(0, findMissionState.get().getSubmittedMissionImage().lastIndexOf("/")));
            if(!uploadFolder.exists()){
                uploadFolder.mkdirs();
            }
            log.info("제출 미션 수정 파일 생성 경로 : " + uploadFolder);

            File beforeMissionImage = new File(rootImagePath + findMissionState.get().getSubmittedMissionImage());
            if(beforeMissionImage.exists()){
                beforeMissionImage.delete();
                log.info("제출 미션 이전 이미지 삭제 완료 : " + beforeMissionImage);
            }

            String fileOriginalName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            File submitMissionImage = new File(uploadFolder, fileOriginalName);
            file.transferTo(submitMissionImage);

            String submitMissionImagePath = uploadFolder.toString().substring(28).replaceAll("\\\\", "/") +  "/" + fileOriginalName;

            // 수정
            findMissionState.get().modifyMyMissionState(requestMissionStateDto.getSubmittedMissionNm(), requestMissionStateDto.getSubmittedMissionDesc(), submitMissionImagePath, requestMissionStateDto.getRejectionYn(), requestMissionStateDto.getRejectionDt(), requestMissionStateDto.getRejectionDesc());
        }

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 마이페이지 - 나의 미션 현황 전체 조회
     * @param userId
     * @param pageable
     * @param search
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ResponseMissionStateDto> findAllMyMissionState(String userId, Pageable pageable, String search){
        return missionStateRepository.findAllMyMissionState(userId, pageable, search);
    }
}

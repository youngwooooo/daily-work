package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionDto;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    @Value("${custom.path.root-image}")
    private String rootImagePath;

    /**
     * 전체 Mission 조회
     * @return 페이징 처리 된 전체 Mission List
     */
    @Transactional(readOnly = true)
    public Page<ResponseMissionDto> findAllMissions(Pageable pageable, String search){
        // 전체 Mission 조회
        Page<Mission> findAllMission = missionRepository.findAllMission(pageable, search);
        return findAllMission.map(ResponseMissionDto::toPaging);
    }
    /**
     * Mission 단건 조회(상세 조회)
     * @param missionSeq
     * @return findMissionToDto
     */
    @Transactional(readOnly = true)
    public ResponseMissionDto findMission(Long missionSeq){
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
                                                .build();

        return findMissionToDto;
    }

    /**
     * 미션시작일 ~ 미션종료일 사이 모든 날짜를 조회 및 포맷
     * @param missionSeq
     * @return
     */
    @Transactional(readOnly = true)
    public List<List<LocalDate>> getDateOfWeek(long missionSeq){

        Optional<Mission> findMission = missionRepository.findById(missionSeq);
        if(!findMission.isPresent()){
            throw new IllegalArgumentException("해당 미션이 존재하지 않습니다. 미션 번호 : " + missionSeq);
        }

        // 미션시작일
        LocalDate missionStDt = LocalDate.from(findMission.get().getMissionStDt());
        // 미션종료일
        LocalDate missionEndDt = LocalDate.from(findMission.get().getMissionEndDt());
        // 총 미션 일수
        long totalDateCont = ChronoUnit.DAYS.between(missionStDt, missionEndDt);

        // 미션시작일 ~ 미션종료일 사이의 모든 날짜를 담을 Array
        List<LocalDate> allDateList = new ArrayList<>();
        // 미션시작일 ~ 미션종료일 사이의 모든 날짜 allDateList에 담기
        for(int i = 0; i <= totalDateCont; i++){
            // 날짜
            LocalDate date = missionStDt.plusDays(i);
            allDateList.add(date);
        }

        // 각 주차별로 날짜를 담을 Array
        List<List<LocalDate>> dateOfWeek = new ArrayList<>();
        // 임시 배열
        List<LocalDate> temp = new ArrayList<>();
        // 주차 별로 끊기 위한 변수
        int cnt = 0;
        // 일요일을 기준으로 주차별로 끊어서 dateOfWeek에 담기
        for(int i=0; i<allDateList.size(); i++){
            temp.add(allDateList.get(i));

            /*
             * 일요일을 기준으로 주차를 끊어주기
             * getDayOfWeek().getValue() : 1 = 월, 2= 화, 3=수 .... 7 = 일
             */
            if(allDateList.get(i).getDayOfWeek().getValue() == 7){
                cnt++;
            }

            if(cnt == 1){
                dateOfWeek.add(temp);
                cnt = 0;
                temp = new ArrayList<>();
            }
        }
        // 나머지 날짜(마지막 주차)가 존재할 경우
        if(temp.size() > 0){
            dateOfWeek.add(temp);
        }

        log.info("dateOfWeek : " + dateOfWeek);

        return dateOfWeek;
    }

    /**
     * 전체 MISSION - 미션 만들기 - [등록]
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

    /**
     * 미션 상세 조회 - [미션 수정하기]
     * @param requestMissionDto
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional
    public String modify(RequestMissionDto requestMissionDto, MultipartFile file) throws IOException {

        // 미션 조회(영속화)
        Optional<Mission> findMission = missionRepository.findById(requestMissionDto.getMissionSeq());
        if(!findMission.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        // 대표 이미지가 존재하지 않을 때
        if(file == null){
            findMission.get().modifyMission(requestMissionDto);
            return ReturnResult.SUCCESS.getValue();
        }

        // 이미지 변경 시
        // 업로드될 폴더 생성
        File uploadFolder = new File(missionUploadPath + requestMissionDto.getMissionSeq());
        if(!uploadFolder.exists()){
            uploadFolder.mkdir();
        }

        File beforeMissionImage = new File(rootImagePath + findMission.get().getMissionImage());
        if(beforeMissionImage.exists()){
            beforeMissionImage.delete();
        }

        // 대표 이미지 이름
        String fileOriginalName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
        // 대표 이미지 생성
        File missionImage = new File(uploadFolder, fileOriginalName);
        file.transferTo(missionImage);

        // DB에 저장할 대표 이미지 경로
        String missionImagePath = "/mission/" + requestMissionDto.getMissionSeq() + "/" + fileOriginalName;

        // requestMissionDto의 missionImage 값 수정
        requestMissionDto.setMissionImage(missionImagePath);

        findMission.get().modifyMission(requestMissionDto);

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 미션 상세 조회 - [미션 삭제하기]
     * @param missionSeq
     * @param delYn
     * @return
     */
    @Transactional
    public String delete(long missionSeq, String delYn){

        Optional<Mission> findMission = missionRepository.findById(missionSeq);
        if(!findMission.isPresent()){
            return ReturnResult.ERROR.getValue();
        }

        findMission.get().deleteMission(delYn);

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 최근 참여 미션 5건 조회
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseMissionDto> findLatelyParticipationMission(String userId) {
        List<Mission> findLatelyParticipationMission = missionRepository.findLatelyParticipationMission(userId);
        return findLatelyParticipationMission.stream().map(ResponseMissionDto::new).collect(Collectors.toList());
    }

    /**
     * 나의 참여 미션 조회
     * @param userId
     * @param pageable
     * @param search
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ResponseMissionDto> findAllLatelyParticipationMission(String userId, Pageable pageable, String search){
        Page<Mission> findAllLatelyParticipationMission = missionRepository.findAllLatelyParticipationMission(userId, pageable, search);
        return findAllLatelyParticipationMission.map(ResponseMissionDto::toPaging);
    }

    /**
     * 최근 작성한 미션 5건 조회
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseMissionDto> findLatelyCreatedMission(String userId) {
        List<Mission> findLatelyCreatedMission = missionRepository.findLatelyCreatedMission(userId);
        return findLatelyCreatedMission.stream().map(ResponseMissionDto::new).collect(Collectors.toList());
    }
}

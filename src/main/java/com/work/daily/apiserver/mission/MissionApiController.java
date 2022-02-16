package com.work.daily.apiserver.mission;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.mission.dto.*;
import com.work.daily.mission.service.MissionParticipantsService;
import com.work.daily.mission.service.MissionService;
import com.work.daily.mission.service.MissionStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MissionApiController {

    private final MissionService missionService;
    private final MissionParticipantsService missionParticipantsService;
    private final MissionStateService missionStateService;

    /**
     * 전체 MISSION - 미션 만들기 - [등록]
     * @description 미션 생성
     * @param requestMissionDto
     * @param file
     * @param bindingResult
     * @return
     * @throws IOException
     */
    @PostMapping("/mission")
    public ResponseEntity<ResponseDto> createMission(@Valid @RequestPart(value = "requestMissionDto") RequestMissionDto requestMissionDto
                                                    , @RequestPart(value = "file", required = false) MultipartFile file
                                                    , BindingResult bindingResult) throws IOException {

        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: createMission :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        String result = missionService.save(requestMissionDto, file);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).data(ReturnResult.ERROR
            ).message("미션 등록에 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("미션 등록이 완료되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 미션 상세 조회 - [미션 수정하기]
     * @description 미션 전체 및 일부 수정(미션명, 미션설명, 대표이미지, 미션종료일, 공개여부, 자동참여 여부)
     * @param missionSeq
     * @param requestMissionDto
     * @param file
     * @param bindingResult
     * @return
     * @throws IOException
     */
    @PatchMapping("/mission/{missionSeq}")
    public ResponseEntity<ResponseDto> modifyMission(@PathVariable("missionSeq") long missionSeq
                                                    , @Valid @RequestPart(value = "requestMissionDto") RequestMissionDto requestMissionDto
                                                    , @RequestPart(value = "file", required = false) MultipartFile file
                                                    , BindingResult bindingResult) throws IOException{
        log.info("미션 수정 데이터 : " + requestMissionDto.toString());
        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: createMission :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        String result = missionService.modify(requestMissionDto, file);
        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("미션이 존재하지 않습니다. 미션번호 : " + requestMissionDto.getMissionSeq()).build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("미션 수정이 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 미션 상세 조회 - [미션 삭제하기] - [삭제]
     * @description 미션의 삭제여부(delYn)를 N으로 수정
     * @param missionSeq
     * @param delYn
     * @return
     */
    @DeleteMapping("/mission/{missionSeq}")
    public ResponseEntity<ResponseDto> deleteMission(@PathVariable(name = "missionSeq") long missionSeq, @RequestParam(name = "delYn") String delYn){

        String result = missionService.delete(missionSeq, delYn);
        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("해당 미션은 존재하지 않습니다. 미션번호 : " + missionSeq).build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("미션 삭제를 완료하였습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 미션 상세 조회 - [미션 참여하기] - [참여]
     * @description 미션의 자동참여 여부(autoAccessYn)가 N일 경우, 미션 참여자 자동 추가
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @return
     */
    @PostMapping("/mission/{missionSeq}/join")
    public ResponseEntity<ResponseDto> joinMission(@PathVariable(name = "missionSeq") long missionSeq
                                                    , @Valid @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto
                                                    , BindingResult bindingResult){
        // 유효성 검사 오류 발생 시
        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: joinMission :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        String result = missionParticipantsService.joinMission(requestMissionParticipantsDto);

        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("이미 참여한 미션입니다.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("미션 참여가 완료 되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 미션 상세 조회 - [미션 탈퇴하기] - [탈퇴]
     * @description 미션의 자동참여 여부(autoAccessYn)가 N일 경우, 미션 참여자 자동 삭제
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @return
     */
    @DeleteMapping("/mission/{missionSeq}/secession")
    public ResponseEntity<ResponseDto> secessionMission(@PathVariable(name = "missionSeq") long missionSeq
                                                        , @Valid @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto
                                                        , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: secessionMission :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        String result = missionParticipantsService.secessionMission(requestMissionParticipantsDto);

        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("미션 참여자가 아닙니다.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("미션 탈퇴가 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 미션 상세 조회 - [미션 참여자 관리] - [승인]
     * @description 미션의 자동참여 여부(autoAccessYn)가 Y일 경우, 미션 생성자가 승인하여 미션 참여자 추가
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @param bindingResult
     * @return
     */
    @PatchMapping("/mission/{missionSeq}/approve-participants")
    public ResponseEntity<ResponseDto> approveParticipants(@PathVariable(name = "missionSeq") long missionSeq
                                                            , @Valid @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto
                                                            , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: approveParticipants :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        ResponseMissionParticipantsDto approveParticipantsInfo = missionParticipantsService.approveParticipants(requestMissionParticipantsDto);

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(approveParticipantsInfo).message("미션 참여자 승인이 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 미션 상세 조회 - [미션 참여자 관리] - [강퇴]
     * @description 미션의 자동참여 여부(autoAccessYn)가 Y일 경우, 미션 생성자가 강퇴하여 미션 참여자 삭제
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @param bindingResult
     * @return
     */
    @DeleteMapping("/mission/{missionSeq}/expulsion-participants")
    public ResponseEntity<ResponseDto> expulsionParticipants(@PathVariable(name = "missionSeq") long missionSeq
                                                                , @Valid @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto
                                                                , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: approveParticipants :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        ResponseMissionParticipantsDto expulsionParticipantsInfo = missionParticipantsService.expulsionParticipants(requestMissionParticipantsDto);

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(expulsionParticipantsInfo).message("미션 참여자 강퇴가 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * [미션 제출] - [제출]
     * @description 미션 결과 제출
     * @param missionSeq
     * @param requestMissionStateDto
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/mission/{missionSeq}/submitMission")
    public ResponseEntity<ResponseDto> createSubmitMission(@PathVariable(name = "missionSeq") long missionSeq
                                                            , @RequestPart(value = "requestMissionStateDto") RequestMissionStateDto requestMissionStateDto
                                                            , @RequestPart(value = "file") MultipartFile file) throws IOException {


        String result = missionStateService.save(requestMissionStateDto, file);

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("미션 제출이 완료되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 미션현황 단건 조회
     * @param missionStateSeq
     * @param missionStateWeek
     * @return
     */
    @GetMapping("/missionState/{missionStateWeek}/{missionStateSeq}")
    public ResponseEntity<ResponseDto> detailMissionState(@PathVariable("missionStateSeq") long missionStateSeq
                                                        , @PathVariable("missionStateWeek") long missionStateWeek){
        ResponseMissionStateDto findOneMissionState = missionStateService.findOneMissionState(missionStateSeq, missionStateWeek);
        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).message("나의 제출 미션 조회 성공").data(findOneMissionState).build(), HttpStatus.OK);
    }

    /**
     * 승인 대기 미션현황 수정
     * @description 1. 승인여부 N -> Y 수정
     *              2. 반려여부 N -> Y, 반려 일자, 반려 내용 수정
     * @param missionStateSeq
     * @param missionStateWeek
     * @return
     */
    @PatchMapping("/missionState/{missionStateWeek}/{missionStateSeq}")
    public ResponseEntity<ResponseDto> modifyMissionState(@PathVariable("missionStateSeq") long missionStateSeq
                                                                    , @PathVariable("missionStateWeek") long missionStateWeek
                                                                    , @RequestBody RequestMissionStateDto requestMissionStateDto){
        if("Y".equals(requestMissionStateDto.getRejectionYn())){
            // 반려여부 N -> Y, 반려 일자, 반려 내용 수정
            String result = missionStateService.modifyMissionStateRejectionInfo(requestMissionStateDto);
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).message("반려 처리가 완료되었습니다.").data(result).build(), HttpStatus.OK);
        }else {
            // 승인여부 N -> Y 수정
            String result = missionStateService.modifyMissionStateApprovalYn(requestMissionStateDto);
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).message("승인이 완료되었습니다.").data(result).build(), HttpStatus.OK);
        }
    }

    /**
     * 나의 제출 미션현황 수정
     * @param missionStateSeq
     * @param missionStateWeek
     * @param requestMissionStateDto
     * @param file
     * @return
     * @throws IOException
     */
    @PatchMapping("/my-missionState/{missionStateWeek}/{missionStateSeq}")
    public ResponseEntity<ResponseDto> modifyMyMissionState(@PathVariable("missionStateSeq") long missionStateSeq
                                                        , @PathVariable("missionStateWeek") long missionStateWeek
                                                        , @RequestPart(value = "requestMissionStateDto") RequestMissionStateDto requestMissionStateDto
                                                        , @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String result = missionStateService.modifyMyMissionState(requestMissionStateDto, file);

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("제출 미션 수정이 완료되었습니다.").build(), HttpStatus.OK);
    }

    // 테스트 용 api
    @GetMapping("/mission/test")
    public Page<ResponseMissionStateDto> test(@PageableDefault(size = 9) Pageable pageable, @RequestParam(required = false, defaultValue = "") String search){
        String userId = "googleUser0";
        return missionStateService.findAllMyMissionState(userId, pageable, search);
    }
}

package com.work.daily.apiserver.mission;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 전체 MISSION - 미션 만들기 - [등록]
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
     * 미션 상세 조회 - [미션 삭제하기]
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
     * 미션 상세 조회 - [미션 참여하기]
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

        String result = missionService.joinMission(requestMissionParticipantsDto);

        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("이미 참여한 미션입니다.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("미션 참여가 완료 되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 미션 상세 조회 - [미션 탈퇴하기]
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @return
     */
    @DeleteMapping("/mission/{missionSeq}/secession")
    public ResponseEntity<ResponseDto> secessionMission(@PathVariable(name = "missionSeq") long missionSeq
                                                        , @Valid @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto
                                                        , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("MissionApiController :: joinMission :: 유효성 검사 오류 = " + bindingResult.getFieldErrors());
            Map<String, Object> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(errorMap).message("입력된 값이 올바르지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }

        String result = missionService.secessionMission(requestMissionParticipantsDto);

        if(ReturnResult.ERROR.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("미션 참여자가 아닙니다.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("미션 탈퇴가 완료되었습니다.").build(), HttpStatus.OK);
    }
}

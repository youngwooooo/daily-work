package com.work.daily.apiserver.mission;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.domain.entity.MissionParticipants;
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
     * 미션 생성
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
     * 미션 참여자 추가
     * @param missionSeq
     * @param requestMissionParticipantsDto
     * @return
     */
    @PostMapping("/mission/{missionSeq}/join")
    public ResponseEntity<ResponseDto> joinMission(@PathVariable(name = "missionSeq") long missionSeq, @RequestBody RequestMissionParticipantsDto requestMissionParticipantsDto){
        log.info("미션 참여자 REQUEST 정보 : " + requestMissionParticipantsDto.toString());

        MissionParticipants missionParticipants = missionService.joinMission(requestMissionParticipantsDto);
        log.info("미션 참여자 SAVE 정보 : " + missionParticipants.toString());

        return null;
    }
}

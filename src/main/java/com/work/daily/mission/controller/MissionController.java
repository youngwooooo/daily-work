package com.work.daily.mission.controller;

import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.mission.dto.ResponseMissionDto;
import com.work.daily.mission.dto.ResponseMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import com.work.daily.mission.service.MissionParticipantsService;
import com.work.daily.mission.service.MissionService;
import com.work.daily.mission.service.MissionStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MissionController {

    private final MissionService missionService;
    private final MissionParticipantsService missionParticipantsService;
    private final MissionStateService missionStateService;

    /**
     * 전체 MISSION 페이지
     * @return 전체 MISSION VIEW
     */
    @GetMapping("/missions")
    public String missions(Model model, @PageableDefault(size = 9) Pageable pageable){
        log.info("MissionController :: missions called");

        Page<ResponseMissionDto> findAllMissions = missionService.findAllMissions(pageable);
        int startPage = 1;
        int endPage = findAllMissions.getTotalPages();
        log.info("startPage : " + startPage);
        log.info("endPage : " + endPage);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("missions", findAllMissions);

        return "contents/mission/missions";
    }

    /**
     * 미션 만들기 페이지
     * @return 미션 만들기 페이지 VIEW
     */
    @GetMapping("/mission")
    public String createMissionForm(){
        log.info("MissionController :: createMissionForm called");
        return "contents/mission/createMission";
    }

    /**
     * 미션 수정 페이지
     * @param missionSeq
     * @param model
     * @return 미션 수정 페이지 VIEW
     */
    @GetMapping("/mission/{missionSeq}/modify")
    public String modifyMission(@PathVariable("missionSeq") long missionSeq, Model model){
        ResponseMissionDto responseMissionDto = missionService.findMission(missionSeq);
        model.addAttribute("mission", responseMissionDto);

        return "contents/mission/modifyMission";
    }

    /**
     * 미션 상세 조회 페이지
     * @param missionSeq
     * @param model
     * @return 미션 상세 조회 페이지 VIEW
     */
    @GetMapping("/mission/{missionSeq}")
    public String detailMission(@PathVariable("missionSeq") long missionSeq, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("MissionController :: detailMission called");

        // 미션
        ResponseMissionDto findMission = missionService.findMission(missionSeq);
        // 미션시작일 ~ 미션종료일
        List<List<LocalDate>> dateOfWeek = missionService.getDateOfWeek(findMission.getMissionSeq());
        // 미션 참여자 List
        List<ResponseMissionParticipantsDto> findAllMissionParticipantByMissionSeq = missionParticipantsService.findAllMissionParticipantByMissionSeq(missionSeq);
        // 모든 미션 현황 List
        List<ResponseMissionStateDto> findAllMissionState = missionStateService.findAllMissionStateByMissionSeq(missionSeq);

        // 세션 존재 시 == 로그인 했을 시
        if(customUserDetails != null) {
            // 회원ID에 따른 나의 제출 미션 List
            List<ResponseMissionStateDto> findMissionStateByMissionSeqAndUserId = missionStateService.findMissionStateByMissionSeqAndUserId(missionSeq, customUserDetails.getLoginUserDto().getUserId());
            model.addAttribute("myMissionState", findMissionStateByMissionSeqAndUserId);
        }

        model.addAttribute("mission", findMission);
        model.addAttribute("dateOfWeek", dateOfWeek);
        model.addAttribute("missionParticipants", findAllMissionParticipantByMissionSeq);
        model.addAttribute("allMissionState", findAllMissionState);

        return "contents/mission/detailMission";
    }
}

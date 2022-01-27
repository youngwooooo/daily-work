package com.work.daily.mission.controller;

import com.work.daily.mission.dto.ResponseMissionDto;
import com.work.daily.mission.dto.ResponseMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import com.work.daily.mission.service.MissionParticipantsService;
import com.work.daily.mission.service.MissionService;
import com.work.daily.mission.service.MissionStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String missions(Model model){
        log.info("MissionController :: missions called");

        List<ResponseMissionDto> findAllMissions = missionService.findAllMissions();
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
    public String detailMission(@PathVariable("missionSeq") long missionSeq, Model model){
        log.info("MissionController :: detailMission called");

        ResponseMissionDto findMission = missionService.findMission(missionSeq);
        List<List<LocalDate>> dateOfWeek = missionService.getDateOfWeek(findMission.getMissionSeq());
        List<ResponseMissionParticipantsDto> findAllMissionParticipantByMissionSeq = missionParticipantsService.findAllMissionParticipantByMissionSeq(missionSeq);
        List<ResponseMissionStateDto> findAllMissionState = missionStateService.findAllMissionStateByMissionSeq(missionSeq);

        model.addAttribute("mission", findMission);
        model.addAttribute("dateOfWeek", dateOfWeek);
        model.addAttribute("missionParticipants", findAllMissionParticipantByMissionSeq);
        model.addAttribute("missionState", findAllMissionState);

        return "contents/mission/detailMission";
    }
}

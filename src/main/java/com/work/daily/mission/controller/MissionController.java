package com.work.daily.mission.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MissionController {

    /**
     * 전체 MISSION 페이지
     * @return 전체 MISSION VIEW
     */
    @GetMapping("/missions")
    public String missions(){
        log.info("MissionController :: missions called");
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
}

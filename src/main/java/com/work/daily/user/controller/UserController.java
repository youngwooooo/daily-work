package com.work.daily.user.controller;

import com.work.daily.login.auth.CustomUserDetails;
import com.work.daily.mission.dto.ResponseMissionDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final MissionService missionService;
    private final MissionStateService missionStateService;

    /**
     * 마이페이지 화면
     * @return 마이페이지 VIEW
     */
    @GetMapping("/user/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        String userId = customUserDetails.getLoginUserDto().getUserId();

        // 최근 참여한 미션
        List<ResponseMissionDto> findLatelyParticipationMission = missionService.findLatelyParticipationMission(userId);
        // 최근 작성한 미션
        List<ResponseMissionDto> findLatelyCreatedMission = missionService.findLatelyCreatedMission(userId);
        // 최근 작성한 게시글

        model.addAttribute("participationMission", findLatelyParticipationMission);
        model.addAttribute("createdMission", findLatelyCreatedMission);

        return "contents/user/myPage";
    }

    /**
     * 마이페이지 - 나의 참여 미션
     * @param customUserDetails
     * @param model
     * @param pageable
     * @param search
     * @return
     */
    @GetMapping("/user/mypage/my-participation-mission")
    public String myParticipationMission(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PageableDefault(size = 8) Pageable pageable, @RequestParam(required = false, defaultValue = "") String search){
        String userId = customUserDetails.getLoginUserDto().getUserId();

        Page<ResponseMissionDto> findAllLatelyParticipationMission = missionService.findAllLatelyParticipationMission(userId, pageable, search);

        int firstPage = 1;  // 첫번째 페이지
        int lastPage = findAllLatelyParticipationMission.getTotalPages(); // 마지막 페이지(미션 전체 개수)
        int startPage = Math.max(firstPage + 1, findAllLatelyParticipationMission.getPageable().getPageNumber() - 2); // 시작 페이지
        int endPage = Math.min(lastPage - 1, findAllLatelyParticipationMission.getPageable().getPageNumber() + 4);    // 끝 페이지

        model.addAttribute("participationMission", findAllLatelyParticipationMission);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);

        return "contents/user/myParticipationMission";
    }

    /**
     * 마이페이지 - 나의 작성 미션
     * @param customUserDetails
     * @param model
     * @param pageable
     * @param search
     * @return
     */
    @GetMapping("/user/mypage/my-created-mission")
    public String myCreatedMission(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PageableDefault(size = 8) Pageable pageable, @RequestParam(required = false, defaultValue = "") String search){
        String userId = customUserDetails.getLoginUserDto().getUserId();

        Page<ResponseMissionDto> findAllLatelyCreatedMission = missionService.findAllLatelyCreatedMission(userId, pageable, search);

        int firstPage = 1;  // 첫번째 페이지
        int lastPage = findAllLatelyCreatedMission.getTotalPages(); // 마지막 페이지(미션 전체 개수)
        int startPage = Math.max(firstPage + 1, findAllLatelyCreatedMission.getPageable().getPageNumber() - 2); // 시작 페이지
        int endPage = Math.min(lastPage - 1, findAllLatelyCreatedMission.getPageable().getPageNumber() + 4);    // 끝 페이지

        model.addAttribute("createdMission", findAllLatelyCreatedMission);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);

        return "contents/user/myCreatedMission";
    }

    /**
     * 마이페이지 - 나의 미션 현황
     * @param customUserDetails
     * @param model
     * @param pageable
     * @param search
     * @return
     */
    @GetMapping("/user/mypage/my-mission-state")
    public String myMissionState(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, @PageableDefault(size = 8) Pageable pageable, @RequestParam(required = false, defaultValue = "") String search){
        String userId = customUserDetails.getLoginUserDto().getUserId();

        Page<ResponseMissionStateDto> findAllMyMissionState = missionStateService.findAllMyMissionState(userId, pageable, search);

        int firstPage = 1;  // 첫번째 페이지
        int lastPage = findAllMyMissionState.getTotalPages(); // 마지막 페이지(미션 전체 개수)
        int startPage = Math.max(firstPage + 1, findAllMyMissionState.getPageable().getPageNumber() - 2); // 시작 페이지
        int endPage = Math.min(lastPage - 1, findAllMyMissionState.getPageable().getPageNumber() + 4);    // 끝 페이지

        model.addAttribute("missionState", findAllMyMissionState);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);

        return "contents/user/myMissionState";
    }

    /**
     * 계정관리 화면
     * @return 계정관리 VIEW
     */
    @GetMapping("/user/my-account")
    public String myAccountForm(){
        return "contents/user/myAccount";
    }

    /**
     * 계정관리(마이페이지) - 비밀번호 확인 - 비밀번호 변경 화면
     * @return 비밀번호 변경 VIEW
     */
    @GetMapping("/user/my-account/password")
    public String modifyPasswordForm(){
        return "contents/user/password";
    }

    /**
     * 계정관리(마이페이지) - 비밀번호 확인 - 비밀번호 변경 - 비밀번호 변경 완료 화면
     * @return 비밀번호 변경 완료 화면
     */
    @GetMapping("/user/my-account/password/success")
    public String modifyPasswordSuccess(){
        return "contents/user/modifyPasswordSuccess";
    }


}

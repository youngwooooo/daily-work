package com.work.daily.user.controller;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardService;
import com.work.daily.board.service.BoardTypeService;
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
    private final BoardService boardService;
    private final BoardTypeService boardTypeService;

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
        List<ResponseBoardDto> findBoardCountTen = boardService.findBoardCountTen(userId);

        model.addAttribute("participationMission", findLatelyParticipationMission);
        model.addAttribute("createdMission", findLatelyCreatedMission);
        model.addAttribute("board", findBoardCountTen);

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
     * 마이페이지 - 나의 게시글
     * @param customUserDetails
     * @param pageable
     * @param search
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/user/mypage/my-board")
    public String myBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails
                            , @PageableDefault(size = 10) Pageable pageable
                            , @RequestParam(required = false, defaultValue = "") String search
                            , @RequestParam(required = false, defaultValue = "") String category
                            ,Model model)
    {
        String userId = customUserDetails.getLoginUserDto().getUserId();

        Page<ResponseBoardDto> findAllMyBoard = boardService.findAllMyBoard(pageable, search, category, userId);
        List<BoardTypeDto> findAllBoardType = boardTypeService.findAllBoardType();

        int firstPage = 1;  // 첫번째 페이지
        int lastPage = findAllMyBoard.getTotalPages(); // 마지막 페이지(게시글 전체 개수)
        int startPage = Math.max(firstPage + 1, findAllMyBoard.getPageable().getPageNumber() - 2); // 시작 페이지
        int endPage = Math.min(lastPage - 1, findAllMyBoard.getPageable().getPageNumber() + 4);    // 끝 페이지
        long totalCount = findAllMyBoard.getTotalElements();

        model.addAttribute("board", findAllMyBoard);
        model.addAttribute("boardType", findAllBoardType);

        model.addAttribute("search", search);
        model.addAttribute("category", category);

        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "contents/user/myBoard";
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

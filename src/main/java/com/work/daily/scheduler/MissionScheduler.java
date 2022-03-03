package com.work.daily.scheduler;

import com.work.daily.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionScheduler {

    private final MissionService missionService;

    /**
     * 미션 마감여부 N -> Y로 변경하는 스케쥴러
     * @description 매일 오전 6시에 미션종료일이 오늘보다 이전인 미션들의 마감여부를 Y로 수정한다.
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void closeMission(){
        log.info("스케쥴러 실행");
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        missionService.closeMission(now);

    }
}

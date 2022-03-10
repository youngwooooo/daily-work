package com.work.daily.mission.repository;

import com.work.daily.TestConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionParticipantsRepository missionParticipantsRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup(){
        User user1 = User.builder().userId("user1").userPw("user11234!").userNm("유저1").role(UserRole.USER).build();
        User user2 = User.builder().userId("user2").userPw("user21234!").userNm("유저2").role(UserRole.USER).build();
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        List<Mission> missionList = new ArrayList<>();
        List<MissionParticipants> missionParticipantsList = new ArrayList<>();

        for(int i=0; i<10; i++){
            Mission mission = Mission.builder()
                    .missionNm((i+1) + "번 미션")
                    .missionDesc((i+1) + "번 미션 내용")
                    .user(savedUser1)
                    .missionStDt(LocalDateTime.now().minusMonths(1))
                    .missionEndDt(LocalDateTime.now().minusDays(1))
                    .missionImage("missionImage" + (i+1) + ".png")
                    .masterYn("Y")
                    .releaseYn("Y")
                    .autoAccessYn("Y")
                    .delYn("N")
                    .temporaryYn("N")
                    .closeYn("N")
                    .build();

            missionList.add(mission);

        }
        List<Mission> savedMissions = missionRepository.saveAll(missionList);

        for(int i=0; i<10; i++){
            MissionParticipants missionParticipants = MissionParticipants.builder()
                    .missionSeq(savedMissions.get(i).getMissionSeq())
                    .userSeq(savedUser2.getUserSeq())
                    .userId(savedUser2.getUserId())
                    .missionJoinYn("Y")
                    .missionJoinApprovalDt(LocalDateTime.now())
                    .user(user2)
                    .build();

            missionParticipantsList.add(missionParticipants);
        }

        List<MissionParticipants> savedParticipants =  missionParticipantsRepository.saveAll(missionParticipantsList);

    }

    /*
        @DataJpaTest는 자동 트렌젝션 롤백이 된다.
        하지만, User와 Mission의 @Id는 자동 증가 방식이 Sequence이기 때문에 1로 초기화 되지 않는다.
        initSequence() : User, Mission의 Sequence를 롤백 시 1로 초기화
     */
    @AfterEach
    public void initSequence(){
        entityManager
                .createNativeQuery("ALTER SEQUENCE USER_SEQ_INCREASE RESTART WITH 1")
                .executeUpdate();
        entityManager
            .createNativeQuery("ALTER SEQUENCE MISSION_SEQ_INCREASE RESTART WITH 1")
            .executeUpdate();

    }

    @Test
    @Order(1)
    @DisplayName("모든 미션 조회(페이징)")
    public void findAllMission(){
        // given
        String search = "";
        Pageable pageable = PageRequest.of(0, 9);

        // when
        Page<Mission> findAllMission = missionRepository.findAllMission(pageable, search);

        // then
        assertThat(findAllMission.getSize()).isEqualTo(9);
        assertThat(findAllMission.getTotalElements()).isEqualTo(10);
    }

    @Test
    @Order(2)
    @DisplayName("미션 법호에 따른 미션 단건(상세) 조회")
    public void findMission(){
        // given
        System.out.println("미션 개수 : " + missionRepository.findAll().size());
        long missionSeq = 1;

        // when
        Optional<Mission> findMission = missionRepository.findMission(missionSeq);

        // then
        assertThat(findMission.get().getMissionSeq()).isEqualTo(missionSeq);
    }

    @Test
    @Order(3)
    @DisplayName("최근 참여한 미션 4건 조회")
    public void findLatelyParticipationMission(){
        // given
        String userId = "user2";

        // when
        List<Mission> findLatelyParticipationMission = missionRepository.findLatelyParticipationMission(userId);

        // then
        assertThat(findLatelyParticipationMission.size()).isEqualTo(4);
    }

    @Test
    @Order(4)
    @DisplayName("나의 참여 미션 전체 조회")
    public void findAllLatelyParticipationMission(){
        // given
        String userId = "user2";
        String search = "";
        Pageable pageable = PageRequest.of(0, 8);

        // when
        Page<Mission> findAllLatelyParticipationMission = missionRepository.findAllLatelyParticipationMission(userId, pageable, search);

        // then
        assertThat(findAllLatelyParticipationMission.getSize()).isEqualTo(8);
        assertThat(findAllLatelyParticipationMission.getTotalElements()).isEqualTo(10);
    }

    @Test
    @Order(5)
    @DisplayName("최근 작성한 미션 4건 조회")
    public void findLatelyCreatedMission(){
        // given
        for(int i=0; i<missionRepository.findAll().size(); i++) {
            System.out.println("미션번호 : " + missionRepository.findAll().get(i).getMissionSeq());
        }

        String userId = "user1";

        // when
        List<Mission> findLatelyCreatedMission = missionRepository.findLatelyCreatedMission(userId);

        // then
        assertThat(findLatelyCreatedMission.size()).isEqualTo(4);
    }

    @Test
    @Order(6)
    @DisplayName("나의 작성 미션 전체 조회")
    public void findAllLatelyCreatedMission(){
        // given
        String userId = "user1";
        Pageable pageable = PageRequest.of(0, 8);
        String search = "";

        // when
        Page<Mission> findAllLatelyCreatedMission = missionRepository.findAllLatelyCreatedMission(userId, pageable, search);

        // then
        assertThat(findAllLatelyCreatedMission.getSize()).isEqualTo(8);
        assertThat(findAllLatelyCreatedMission.getTotalElements()).isEqualTo(10);

    }

    @Test
    @Order(7)
    @DisplayName("마감여부가 N이고 미션종료일이 오늘보다 이전인 미션 전체 조회")
    public void findAllMissionForClose(){
        // given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        // when
        List<Mission> findAllMissionForClose = missionRepository.findAllMissionForClose(now);

        // then
        assertThat(findAllMissionForClose.size()).isEqualTo(10);
    }

}
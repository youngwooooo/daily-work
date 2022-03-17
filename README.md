# Daily Work(개인 목표 달성 플랫폼)

> **자신의 목표를 미션으로 등록하고 개인 또는 여러 사람과 함께 목표를 성취할 수 있는 자기 계발 플랫폼**
> 
> 
>  **GitHub**  : https://github.com/youngwooooo/daily-work.git
> 

---

- **개발 목적**
    - 취업 준비를 하며 일일 또는 기간을 정해 해야 할 일의 목표를 정해두고 실천하는 규칙적인 생활의 필요성을 느끼게 되었습니다.
    - 내가 설정한 목표를 눈으로 직접 확인하고 기록할 수 있다면 보다 큰 성취감을 얻을 수 있다고 생각하였습니다.
    - Spring Boot, JPA, Security 등의 기술들을 약 1개월 간 학습하였고 직접 활용하고 경험하여 개발 역량을 키우고자 하였습니다.
    
- **개발 환경**
    - Java 1.8
    - Spring Boot (API Sever)
    - Spring Security (Security)
    - Spring Data JPA & QueryDSL (ORM)
    - H2 (RDBMS)
    - OAuth2.0 (Login)
    - Thymeleaf (View)
    - Junit (Test)

# Project Description

---

# 1. 회원 가입

> 회원 가입을 통해 계정을 생성할 수 있습니다.
> 

---

![1 회원가입](https://user-images.githubusercontent.com/80368534/158803203-3f215570-a296-4e25-ba6a-3db0464b8c50.PNG)

---

# 2. 로그인

> 일반 로그인 또는 소셜 로그인(구글, 카카오, 네이버)을 할 수 있습니다.
> 

---

![2 로그인](https://user-images.githubusercontent.com/80368534/158803397-24fd7c0c-5bf6-4731-8569-af6439928e8e.PNG)

---

# 3. 계정 관리

> 사용자 정보를 변경할 수 있습니다.
> 

---

![3 계정관리(일반-1)](https://user-images.githubusercontent.com/80368534/158803522-a19b8b3d-763d-434a-b96f-80833566a562.PNG)

![3 계정관리(일반-2)](https://user-images.githubusercontent.com/80368534/158803580-e70b145f-d5a4-4372-92de-65e0b21c378f.PNG)

---

# 4. 홈(미션)

> 등록된 모든 미션에 대해 조회 할 수 있습니다.
> 
> 
> 미션 제목, 작성자 이름을 통해 미션을 검색할 수 있습니다.
> 

---

![4 홈(미션)](https://user-images.githubusercontent.com/80368534/158803679-3731f196-b4cc-496c-b4ff-12a3916effbc.PNG)

---

# 5. 미션 만들기

> 미션을 임시저장 및 등록할 수 있습니다.
> 
> 
> 미션 대표 이미지와 미션 종료일을 설정할 수 있습니다.
> 
> 공개 여부, 자동참여 여부를 설정할 수 있습니다.
> 

---

![미션 등록](https://user-images.githubusercontent.com/80368534/158803761-9ce8e538-94ae-4b06-a048-92c8ca8af4b7.PNG)

---

# 6. 미션 상세 조회

---

<aside>
💡 미션 생성자(방장)

</aside>

> 미션 참여자 관리, 미션 수정 및 삭제를 할 수 있습니다.
> 

---

![5 미션상세조회-미션생성자](https://user-images.githubusercontent.com/80368534/158803803-211e980c-bc4e-434e-a844-5f4f9c9ebbf4.PNG)

![5 미션상세조회-미션생성자-미션참여자관리](https://user-images.githubusercontent.com/80368534/158803849-6b55b8d3-16ce-4626-9674-6073310693d2.PNG)

![5 미션상세조회-미션생성자-미션삭제](https://user-images.githubusercontent.com/80368534/158803918-f8b8dae3-1e86-4f0a-8c93-858f99d2a9f0.PNG)

> 제출된 미션을 승인 및 반려를 할 수 있습니다.
> 

---

![5 미션상세조회-미션생성자-승인대기미션](https://user-images.githubusercontent.com/80368534/158803966-72c2539d-889e-4825-b838-59680c142a93.PNG)

---

<aside>
💡 미션 참여자

</aside>

> 각 주차별로 승인이 완료된 미션참여자들의 모든 미션현황을 확인할 수 있습니다.
> 
> 
> **미제출(X) / 제출(O) / 승인(체크)**
> 

---

![5 미션상세조회-미션현황](https://user-images.githubusercontent.com/80368534/158804039-6255e526-d802-4319-812e-8d040eb63ead.PNG)

![미션현황3](https://user-images.githubusercontent.com/80368534/158804072-e0ffb662-552e-4183-a7ab-a51c4c8af445.PNG)

![5 미션상세조회-미션현황-2](https://user-images.githubusercontent.com/80368534/158804166-9b3242bc-ec5b-4341-bb03-05c660c7a9ee.PNG)

> 미션현황을 작성할 수 있습니다.
> 
> 
> 또한, 작성한 미션현황은 ‘나의 제출 미션’에서 확인 및 수정이 가능합니다.(**’승인’**된 제출 미션은 수정 불가)
> 

---

![5 미션상세조회-미션제출](https://user-images.githubusercontent.com/80368534/158804264-3f554a89-dd2d-46c6-81a0-1025ebdfd6fb.PNG)

![5 미션상세조회-나의제출미션](https://user-images.githubusercontent.com/80368534/158804331-ee3fb47a-edbd-4d23-bfd0-026961bbbc35.PNG)

---

# 7. 커뮤니티(게시글)

> 등록된 모든 게시글을 조회할 수 있습니다.
> 
> 
> 분류, 게시글 제목, 작성자 이름을 통해 검색을 할 수 있습니다.
> 

---

![6 커뮤니티(복사)](https://user-images.githubusercontent.com/80368534/158804578-249c05df-39d4-43cd-b1e5-6e42060bb324.png)

---

# 8. 글 쓰기

> 게시글을 등록할 수 있습니다.
> 
> 
> 파일은 개수에 상관없이 10MB까지 첨부가 가능합니다.
> 

---

![게시글 등록](https://user-images.githubusercontent.com/80368534/158810942-336e4fb2-b4ca-48be-9014-56d3e7d0e840.PNG)

---

# 9. 게시글 상세 조회

> 게시글에 대한 상세 정보를 확인할 수 있습니다.
> 
> 
> 댓글 및 답글을 작성/수정/삭제할 수 있습니다.
> 
> 첨부파일을 다운로드 할 수 있습니다.
> 

---

![7 게시글 상세 조회](https://user-images.githubusercontent.com/80368534/158811015-05d61fab-8307-4fd0-b585-2d6d8bdfcaba.PNG)

> **게시글 작성자**는 게시글 수정 및 삭제를 할 수 있습니다.
> 
> 
> 해당 게시글의 모든 댓글 삭제를 할 수 있습니다.
> 

---

![7 게시글 상세 조회-게시글 삭제](https://user-images.githubusercontent.com/80368534/158811045-d7de18be-02f0-4b6b-86bd-a2425f08c80d.PNG)

![7 게시글 상세 조회-댓글 삭제](https://user-images.githubusercontent.com/80368534/158811063-66c23f5c-455b-44e5-b667-275e8a1bd3a8.PNG)

---

# 10. 마이페이지

> 계정 관리를 통해 사용자 정보를 수정할 수 있습니다.
> 
> 
> 최근 참여한 미션 / 최근 작성한 미션 / 최근 작성한 게시글을 확인할 수 있습니다.
> 

---

![7 마이페이지](https://user-images.githubusercontent.com/80368534/158811147-98ff0514-b668-4f4e-851c-76c4dc618a1c.PNG)

---

> **자신이 참여한 미션, 자신이 작성한 미션**을 최신 순으로 조회하고 검색할 수 있습니다.
> 
> 
> 미션 클릭 시, 해당 미션 상세 조회 페이지로 이동할 수 있습니다
> 

---

![7.마이페이지-나의참여미션.PNG](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/02dcb1bd-d43d-439f-9673-02b86ec3bc6f/7.마이페이지-나의참여미션.png)

![7.마이페이지-나의작성미션.PNG](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3f7e191c-d4b3-4e7c-9034-f044e3aa940e/7.마이페이지-나의작성미션.png)

---

> **자신이 제출한 미션 현황**을 최신 순으로 조회하고 검색할 수 있습니다.
> 
> 
> 미션 현황 클릭 시, 해당 미션 현황을 상세 조회할 수 있습니다.
> 

---

![7 마이페이지-나의참여미션](https://user-images.githubusercontent.com/80368534/158811195-d46f4b58-0316-4857-8532-99a753c2561e.PNG)

![7 마이페이지-나의작성미션](https://user-images.githubusercontent.com/80368534/158811206-c854bfc3-2e9e-48dd-9825-b688909feefd.PNG)

---

> **자신이 작성한 게시글**을 조회/검색/수정/삭제할 수 있습니다.
> 
> 
> 게시글 클릭 시, 해당 게시글 상세 조회 페이지로 이동할 수 있습니다.
> 

---

![7 마이페이지-나의게시글](https://user-images.githubusercontent.com/80368534/158811352-14874d6f-3838-4b6f-af02-dacfbe3d4a92.PNG)

![7 마이페이지-나의게시글-삭제](https://user-images.githubusercontent.com/80368534/158811247-ce6be45e-ffed-4ef6-b192-f27bef731928.PNG)

---

# Project Structure

---

# 1. Spring Boot (API Server)

---

> 도메인 패키지 구조로 설계하였습니다.
> 
> 
> ( 각 도메인 별 하위 구조로 Controller, Dto, Service 가 존재합니다. )
> 

---

- **aceess**  : 공통적으로 접근할 수 있는 API
- **apiserver**  : 각각의 Entity별 Rest Controller를 관리
- **board**  : Board Entity와 관련된 API
- **boardfile**  : BoardFile Entity와 관련된 API
- **config**  : 프로젝트의 Configuration
- **domain**  : 프로젝트의 entity, pk ( 복합키 ), repository ( JPA & QueryDSL )
- **exception**  : exception 관리
- **login**  : 스프링 시큐리티 로그인 & OAuth2.0 로그인 ( 구글 / 카카오 / 네이버 ) API
- **mission**  : Mission Entity와 관련된 API
- **scheduler**  : 스프링 스케쥴러
- **user**  : User Entity와 관련된 API

> 수정, 삭제와 관련된 비즈니스 로직은 Service가 아닌 domain에 작성하였습니다.
> 

---

```java
// User
user.modifyPassword() // 비밀번호 수정
user.modifyUserInfo() // 회원 정보 수정 (이름, 이메일, 프로필 사진)

// Mission
mission.modifyMissionImage() // 미션 대표 이미지 수정
mission.modifyMission() // 미션 수정 (제목, 내용, 시작일, 종료일, 공개여부, 자동참여 여부, 임시여부, 미션 대표 이미지)
mission.deleteMission() // 미션 삭제 (삭제 여부 Y로 변경)
mission.modifyCloseYn() // 미션 마감 (마감 여부 Y로 변경)

// MissionParticipants
missionParticipants.approveParticipants() // 참여여부, 참여승인일 수정

// MissionState
missionState.modifyMissionStateApprovalYn() // 승인 여부 수정
missionState.modifyMissionStateRejectionInfo() // 반려 여부, 반려 일자, 반려 내용 수정
missionState.modifyMyMissionState() // 미션 현황 수정 (제목, 내용, 이미지, 반려 여부, 반려 일자, 반려 내용)

// Board
board.increaseViewCount() // 조회수 증가
board.deleteBoard() // 게시글 삭제 (삭제 여부 Y로 변경)
board.modifyBoard() // 게시글 수정 (제목, 내용, 분류, 임시여부)

// BoardComment
boardComment.modifyBoardComment() // 댓글/답글 수정
boardComment.deleteBoardComment() // 댓글/답글 삭제 (삭제 여부 Y로 변경)
```

# 2. Spring Security

---

> Spring Security 설정을 통해 권한에 따른 API 접근을 할 수 있도록 하였습니다.
> 

---

```
// csrf 토큰 비활성화
http.csrf().disable();
// 중복 로그인을 위한 세션 처리
http.sessionManagement()
				.maximumSessions(1)                  /* session 최대 허용 개수 */
        .maxSessionsPreventsLogin(false)     /* false : 중복 로그인 시 기존 로그인 종료 , true : 중복 로그인 시 에러 발생 */
        .sessionRegistry(sessionRegistry())  /* 모든 세션 정보를 가짐 */
        .expiredUrl("/");                    /* 세션 만료시 이동 url */

http.authorizeRequests()
						.antMatchers("/user/**", "/boards", "/board/**").hasRole("USER")  /* USER 권한을 가진 사용자만 접근 가능 */
            .antMatchers("/admin/**").hasRole("ADMIN")                        /* ADMIN 권한을 가진 사용자만 접근 가능 */
            .anyRequest().permitAll()                                         /* 이 외의 접근 설정은 모두 접근할 수 있도록 함 */
        .and()
				.logout()                           /* 로그아웃 설정 */
            .logoutSuccessUrl("/missions")  /* 로그아웃 url */
            .invalidateHttpSession(true)    /* 로그아웃 시 세션 제거 */
            .deleteCookies("JSESSIONID")    /* 로그아웃 시 쿠키 제거 */
            .clearAuthentication(true)      /* 로그아웃 시 권한 제거 */
        .and()
				.formLogin()                                          /* form 로그인 설정 */
            .loginPage("/login")                              /* 로그인 페이지 */
            .usernameParameter("userId")                      /* username 파라미터 이름 변경 */
            .passwordParameter("userPw")                      /* password 파라미터 이름 변경 */
            .loginProcessingUrl("/login")                     /* 로그인 성공 시 url */
            .failureHandler(new CustomLoginFailHandler())     /* 로그인 실패 handler */
            .successHandler(new CustomLoginSuccessHandler())  /* 로그인 성공 handler */
        .and()
				.oauth2Login()                              /* oauth2 로그인 설정 */
            .loginPage("/login")                    /* 로그인 페이지 */
            .userInfoEndpoint()                     /* oauth2 로그인 성공 이후 설정 */
            .userService(customOauth2UserService);  /* oauth2 로그인 service */
```

# 3. OAuth2.0

---

> 사용자에게 간편한 구글/카카오/네이버 소셜 로그인을 제공합니다. ( 요청 정보: 이름/이메일/프로필 사진 )
> 

---

![oauth2 인증 과정.PNG](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8022b51a-61c0-467e-824e-1426662d3cf8/oauth2_인증_과정.png)

# 4. Spring Data JPA & QueryDSL

---

> Spring Data JPA의 객체 중심 domain 설계를 지향하고 반복적인 CRUD 작업을 대체하였습니다.
> 
> 
> Join, DTO 조회, 여러 개의 조건이 있는 SQL 등을 QueryDSL로 해결하였습니다.
> 

---

- **Mission**  ( Domain Class )
    
    ```java
    @Getter
    @NoArgsConstructor
    @SequenceGenerator(
            name = "MISSION_SEQ_INCREASE",
            sequenceName = "MISSION_SEQ_INCREASE", // 시퀸스 명
            initialValue = 1, // 초기 값
            allocationSize = 1 // 미리 할당 받을 시퀸스 수
    )
    @AllArgsConstructor
    @Builder
    @Entity
    @Table(name = "TB_MISSION_INFO")
    public class Mission extends BaseTime {
    		............
    }
    ```
    

- **MissionRepository**  ( JPA Interface )
    
    ```java
    public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {
    		............
    }
    ```
    

- **MissionRepositoryCustom**  ( QueryDSL Interface )
    
    ```java
    public interface MissionRepositoryCustom {
    		............
    }
    ```
    

- **MissionRepositoryImple**  ( QueryDSL Implements Class )
    
    ```java
    @RequiredArgsConstructor
    @Repository
    @Slf4j
    public class MissionRepositoryImpl implements MissionRepositoryCustom {
    		............
    }
    ```
    

# 5. Junit5

---

> Controller / Service / Repository 각 Layer별로 Mokito를 활용한 단위 테스트 코드를 작성하였습니다.
> 

---

## 1) **Controller**

- **@WebMvcTest**
    - Controller, web과 관련된 여러 설정(Configuration, Bean 등)만을 스캔하여 띄운다.
    - 간단하게 Controller에 예상되는 요청/응답을 테스트를 하기 위함.
- **@MockBean**
    - 테스트하고자 하는 클래스에 의존성 주입이 되어있는 Bean들을 가짜 객체로 만들어준다.
    - 즉, Controller의 의존성 주입된 Service를 가짜 객체로 만든다.
- **@TestMethodOrder**  : 테스트 코드에 순서를 부여하기 위함.
- **MockMvc**  : 테스트를 위한 MVC환경을 만들어 요청 및 전송, 응답 기능을 제공함

```java
@WebMvcTest(controllers = MissionApiController.class
        , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)) // SecurityConfig.class의 Bean이 필요함 → 에러 발생 → SecurityConfig.class를 제외함
@MockBean(JpaMetamodelMappingContext.class) // JPA 관련 Bean 스캔 X → 에러 발생 → @MockBean으로 가짜 객체 생성
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionApiController :: 단위 테스트")
class MissionApiControllerTest {

		@Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private MissionParticipantsService missionParticipantsService;

    @MockBean
    private MissionStateService missionStateService;

		............
}
```

```java
@WebMvcTest(controllers = MissionController.class
            , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)) // SecurityConfig.class의 Bean이 필요함 → 에러 발생 → SecurityConfig.class를 제외함
@MockBean(JpaMetamodelMappingContext.class) // JPA 관련 Bean 스캔 X → 에러 발생 → @MockBean으로 가짜 객체 생성
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionController :: 단위 테스트")
class MissionControllerTest {

		@Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private MissionParticipantsService missionParticipantsService;

    @MockBean
    private MissionStateService missionStateService;

		............
}
```

## 2) Service

- **@ExtendWith**  : Mokito의 Mock 객체를 사용하기 위함
- **@Mock**
    - 테스트하고자 하는 클래스에 의존성 주입이 되어있는 Bean들을 가짜 객체로 만들어준다.
    - 즉, Service에 의존성 주입된 Repository를 가짜 객체로 만든다.
- **@InjectMocks**  : 생성된 Mock 객체를 해당 객체에 주입시킨다.

```java
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionService :: 단위 테스트")
public class MissionServiceTest {
    private final Logger log = LoggerFactory.getLogger(MissionServiceTest.class);

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionParticipantsRepository missionParticipantsRepository;

    @Mock
    private MissionStateRepository missionStateRepository;

    @InjectMocks
    private MissionService missionService;

		............
}
```

## 3) Repository

- **@DataJpaTest**  : JPA와 관련된 여러 설정(Configuration, Bean 등)만을 스캔하여 띄운다.
- **@Import**
    - TestConfiguration으로 설정한 클래스를 import 시켜준다.
    - QueryDSL을 사용하기 때문에 JPAQueryFactory를 Bean으로 등록이 필요 → TestConfig.class에 등록하고 import 한다.
- **@AutoConfigureTestDatabase**
    - 테스트를 위한 DB 설정
    - @AutoConfigureTestDatabase(replace = Replace.NONE) : application.yml에 설정한 DB를 연동한다.

```java
@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
```

```java
@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionRepository :: 단위 테스트")
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionParticipantsRepository missionParticipantsRepository;

    @Autowired
    private EntityManager entityManager;

		............
}
```

- **프로젝트를 통해 배운점**
    1. **Spring Boot의 장점**
        - spring-boot-starter를 통해 모듈에 대한 의존성을 추가하면 해당 모듈에 관련된 라이브러리도 자동으로 가져와서 쉽게 활용할 수 있다.
        - dependency의 버전 관리는 Spring boot의 버전에 따라 권장 버전으로 설정되어 자동 관리가 가능하다.
        - 내장 WAS를 통해 따로 설치해주지 않아도 바로 application을 실행시킬 수 있다.
        - Configuration, Bean 등을 통한 기본 설정을 자바 코드가 아닌 application.yml 파일로 설정을 할 수 있다.
        
    2. **Spring Data JPA와 QueryDSL에 대한 이해**
        - CRUD 로직에 관한 SQL을 반복적으로 작성할 필요가 없다.
            
            → JPA가 구현한 간단한 메서드를 활용
            
        - 복잡한 SELECT Query는 Native Query로 직접 작성할 수 있다. 하지만 오타 발생 시, 런타임 시점에서만 오류를 확인할 수 있다.
        - QueryDSL을 사용하면 복잡한 Native Query를 자바 코드로 가독성 있게 작성할 수 있다.
        
    3. **Spring Security의 필요성**
        - Spring Security을 통한 설정으로 권한에 따른 특정 API에 접근할 수 있도록 할 수 있다.
            
            →  즉, 세션의 존재여부나 값에 따라 화면이나 서버에서 반복적으로 처리가 필요가 없다.
            
        - 비밀번호 해쉬화를 통해 보안성을 높일 수 있다.
        
    4. **OAuth2.0 로그인**
        - OAuth2.0의 인증 과정에 대한 개념을 정립할 수 있었다.
        - OAuth2.0 로그인 사용 시, Server는 중개자의 역할을 한다.
        
    5. **Thymeleaf**
        - Spring Boot에서 jsp는 여러 제한 사항이 있어 사용을 지양하고, 뷰 템플릿 엔진을 사용하도록 지향한다.
        - Thymeleaf는 많은 뷰 템플릿 엔진 중의 하나이다.
        - 조건문, 반복문, 경로 지정 등 여러 문법에 대한 기초를 배울 수 있었다.
    
    6. **Junit 테스트 코드**
    	- 테스트 코드를 먼저 작성하면 기능의 추가, 수정, 삭제와 예상치 못한 오류에 대해 유연하게 대처할 수 있다.
    	- 모든 기능을 개발하고 테스트 코드를 작성해서 기존의 개발한 기능만을 테스트한 점이 아쉬웠다.
    	- Controller, Service, Repository 각 Layer에 대한 단위 테스트 코드를 작성할 수 있게 되었다.

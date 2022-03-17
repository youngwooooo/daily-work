package com.work.daily.board.repository;

import com.work.daily.TestConfig;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardType;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardRepository;
import com.work.daily.domain.repository.BoardTypeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardRepository :: 단위 테스트")
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardTypeRepository boardTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup(){
        // User
        User user1 = User.builder().userSeq(1).userId("user1").userNm("유저1").userPw("user11234!").role(UserRole.USER).build();
        User savedUser = userRepository.save(user1);

        // BoardType
        List<BoardType> boardTypeList = new ArrayList<>();

        BoardType boardType1 = BoardType.builder().boardTypeSeq(1).boardTypeNm("일반").boardTypeCode("NORMAL").build();
        BoardType boardType2 = BoardType.builder().boardTypeSeq(2).boardTypeNm("질문").boardTypeCode("QA").build();
        BoardType boardType3 = BoardType.builder().boardTypeSeq(3).boardTypeNm("홍보").boardTypeCode("PR").build();
        boardTypeList.add(boardType1);
        boardTypeList.add(boardType2);
        boardTypeList.add(boardType3);

        List<BoardType> savedBoardTypeList = boardTypeRepository.saveAll(boardTypeList);

        // Board
        List<Board> boardList = new ArrayList<>();
        for(long i=0; i<10; i++){
            Board board = Board.builder()
                            .boardSeq(i+1)
                            .boardNm((i+1) + "번 게시글")
                            .boardDesc((i+1) + "번 게시글 내용")
                            .boardType(savedBoardTypeList.get(0).getBoardTypeCode())
                            .user(savedUser)
                            .temporaryYn("N")
                            .delYn("N")
                            .build();

            boardList.add(board);
        }
        for(long i=10; i<20; i++){
            Board board = Board.builder()
                            .boardSeq(i+1)
                            .boardNm((i+1) + "번 게시글")
                            .boardDesc((i+1) + "번 게시글 내용")
                            .boardType(savedBoardTypeList.get(1).getBoardTypeCode())
                            .user(savedUser)
                            .temporaryYn("N")
                            .delYn("N")
                            .build();

            boardList.add(board);
        }
        for(long i=20; i<30; i++){
            Board board = Board.builder()
                            .boardSeq(i+1)
                            .boardNm((i+1) + "번 게시글")
                            .boardDesc((i+1) + "번 게시글 내용")
                            .boardType(savedBoardTypeList.get(2).getBoardTypeCode())
                            .user(savedUser)
                            .temporaryYn("N")
                            .delYn("N")
                            .build();

            boardList.add(board);
        }
        List<Board> savedBoardList = boardRepository.saveAll(boardList);
    }

    @AfterEach
    public void initSequence(){
        entityManager
                .createNativeQuery("ALTER SEQUENCE USER_SEQ_INCREASE RESTART WITH 1")
                .executeUpdate();
        entityManager
                .createNativeQuery("ALTER SEQUENCE BOARD_SEQ_INCREASE RESTART WITH 1")
                .executeUpdate();
        entityManager
                .createNativeQuery("ALTER SEQUENCE BOARD_TYPE_SEQ_INCREASE RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @Order(1)
    @DisplayName("게시글 전체 조회")
    public void findAllBoard() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "";
        String category = "";
        String category1 = "NORMAL";
        String category2 = "QA";
        String category3 = "PR";

        // when
        Page<ResponseBoardDto> findAllBoard = boardRepository.findAllBoard(pageable, search, category);
        Page<ResponseBoardDto> findAllBoardWithCategory1 = boardRepository.findAllBoard(pageable, search, category1);
        Page<ResponseBoardDto> findAllBoardWithCategory2 = boardRepository.findAllBoard(pageable, search, category2);
        Page<ResponseBoardDto> findAllBoardWithCategory3 = boardRepository.findAllBoard(pageable, search, category3);

        // then
        assertThat(findAllBoard.getSize()).isEqualTo(10);
        assertThat(findAllBoardWithCategory1.getSize()).isEqualTo(10);
        assertThat(findAllBoardWithCategory2.getSize()).isEqualTo(10);
        assertThat(findAllBoardWithCategory3.getSize()).isEqualTo(10);

        assertThat(findAllBoard.getTotalElements()).isEqualTo(30);
        assertThat(findAllBoardWithCategory1.getTotalElements()).isEqualTo(10);
        assertThat(findAllBoardWithCategory2.getTotalElements()).isEqualTo(10);
        assertThat(findAllBoardWithCategory3.getTotalElements()).isEqualTo(10);

        assertThat(findAllBoardWithCategory1.getContent().get(0).getBoardType()).isEqualTo(category1);
        assertThat(findAllBoardWithCategory2.getContent().get(0).getBoardType()).isEqualTo(category2);
        assertThat(findAllBoardWithCategory3.getContent().get(0).getBoardType()).isEqualTo(category3);
    }

    @Test
    @Order(2)
    @DisplayName("게시글 단건(상세) 조회")
    public void findOneBoard() {
        // given
        long boardSeq = 1;

        // when
        Optional<Board> findOneBoard = boardRepository.findOneBoard(boardSeq);

        // then
        assertThat(findOneBoard.get().getBoardSeq()).isEqualTo(boardSeq);
        assertThat(findOneBoard.get().getBoardType()).isEqualTo("NORMAL");
    }

    @Test
    @Order(3)
    @DisplayName("최근 작성한 게시글 10건 조회")
    public void findBoardCountTen() {
        // given
        String userId = "user1";

        // when
        List<ResponseBoardDto> findBoardCountTen = boardRepository.findBoardCountTen(userId);

        // then
        assertThat(findBoardCountTen.size()).isEqualTo(10);
        for(int i=0; i<findBoardCountTen.size(); i++){
            assertThat(findBoardCountTen.get(i).getUserId()).isEqualTo(userId);
        }
    }

    @Test
    @Order(4)
    @DisplayName("나의 게시글 전체 조회")
    public void findAllMyBoard() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "";
        String category = "";
        String category1 = "NORMAL";
        String category2 = "QA";
        String category3 = "PR";
        String userId = "user1";

        // when
        Page<ResponseBoardDto> findAllMyBoard = boardRepository.findAllMyBoard(pageable, search, category, userId);
        Page<ResponseBoardDto> findAllMyBoardWithCategory1 = boardRepository.findAllMyBoard(pageable, search, category1, userId);
        Page<ResponseBoardDto> findAllMyBoardWithCategory2 = boardRepository.findAllMyBoard(pageable, search, category2, userId);
        Page<ResponseBoardDto> findAllMyBoardWithCategory3 = boardRepository.findAllMyBoard(pageable, search, category3, userId);

        // then
        assertThat(findAllMyBoard.getSize()).isEqualTo(10);
        assertThat(findAllMyBoardWithCategory1.getSize()).isEqualTo(10);
        assertThat(findAllMyBoardWithCategory2.getSize()).isEqualTo(10);
        assertThat(findAllMyBoardWithCategory3.getSize()).isEqualTo(10);

        for(int i=0; i<findAllMyBoard.getSize(); i++){
            assertThat(findAllMyBoard.getContent().get(i).getUserId()).isEqualTo(userId);
            assertThat(findAllMyBoardWithCategory1.getContent().get(i).getUserId()).isEqualTo(userId);
            assertThat(findAllMyBoardWithCategory2.getContent().get(i).getUserId()).isEqualTo(userId);
            assertThat(findAllMyBoardWithCategory3.getContent().get(i).getUserId()).isEqualTo(userId);
        }

        assertThat(findAllMyBoard.getTotalElements()).isEqualTo(30);
        assertThat(findAllMyBoardWithCategory1.getTotalElements()).isEqualTo(10);
        assertThat(findAllMyBoardWithCategory2.getTotalElements()).isEqualTo(10);
        assertThat(findAllMyBoardWithCategory3.getTotalElements()).isEqualTo(10);

        assertThat(findAllMyBoardWithCategory1.getContent().get(0).getBoardType()).isEqualTo(category1);
        assertThat(findAllMyBoardWithCategory2.getContent().get(0).getBoardType()).isEqualTo(category2);
        assertThat(findAllMyBoardWithCategory3.getContent().get(0).getBoardType()).isEqualTo(category3);
    }
}
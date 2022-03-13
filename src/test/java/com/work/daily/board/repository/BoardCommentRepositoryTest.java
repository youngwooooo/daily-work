package com.work.daily.board.repository;

import com.work.daily.TestConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.BoardType;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardCommentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardCommentRepository :: 단위 테스트")
public class BoardCommentRepositoryTest {

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardTypeRepository boardTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup(){
        // User
        User user1 = User.builder().userSeq(1).userId("user1").userNm("유저1").userPw("user11234!").role(UserRole.USER).build();
        User user2 = User.builder().userSeq(1).userId("user2").userNm("유저2").userPw("user21234!").role(UserRole.USER).build();
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        // BoardType
        BoardType boardType = BoardType.builder().boardTypeSeq(1).boardTypeNm("일반").boardTypeCode("NORMAL").build();
        BoardType savedBoardType = boardTypeRepository.save(boardType);

        // Board
        Board board = Board.builder()
                .boardSeq(1)
                .boardNm(1 + "번 게시글")
                .boardDesc(1 + "번 게시글 내용")
                .boardType(savedBoardType.getBoardTypeCode())
                .user(savedUser1)
                .temporaryYn("N")
                .delYn("N")
                .build();

        Board savedBoard = boardRepository.save(board);

        // BoardComment
        BoardComment parentBoardComment = BoardComment.builder()
                                        .board(savedBoard)
                                        .commentSeq(1)
                                        .commentDesc("댓글")
                                        .user(savedUser1)
                                        .parentCommentSeq(0)
                                        .delYn("N")
                                        .build();

        BoardComment savedParentBoardComment = boardCommentRepository.save(parentBoardComment);

        List<BoardComment> childBoardCommentList = new ArrayList<>();
        for(int i=0; i<10; i++){
            BoardComment childBoardComment = BoardComment.builder()
                                                .board(savedBoard)
                                                .commentSeq(i+2)
                                                .commentDesc((i+1) + "번째 답글")
                                                .user(savedUser2)
                                                .parentCommentSeq(savedParentBoardComment.getCommentSeq())
                                                .delYn("N")
                                                .build();

            childBoardCommentList.add(childBoardComment);
        }
        boardCommentRepository.saveAll(childBoardCommentList);
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
        entityManager
                .createNativeQuery("ALTER SEQUENCE BOARD_COMMENT_SEQ_INCREASE RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @Order(1)
    @DisplayName("댓글 전체 조회")
    public void findAllParentBoardComment(){
        // given
        long boardSeq = 1;
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardComment> findAllParentBoardComment = boardCommentRepository.findAllParentBoardComment(boardSeq, pageable);

        // then
        assertThat(findAllParentBoardComment.getSize()).isEqualTo(10);
        assertThat(findAllParentBoardComment.getTotalElements()).isEqualTo(1);
        assertThat(findAllParentBoardComment.getContent().get(0).getParentCommentSeq()).isEqualTo(0);
        assertThat(findAllParentBoardComment.getContent().get(0).getUser().getUserId()).isEqualTo("user1");
    }

    @Test
    @Order(2)
    @DisplayName("답글 전체 조회")
    public void findAllChildBoardComment(){
        // given
        long boardSeq = 1;

        // when
        List<BoardComment> findAllChildBoardComment = boardCommentRepository.findAllChildBoardComment(boardSeq);

        // then
        assertThat(findAllChildBoardComment.size()).isEqualTo(10);
        for(int i=0; i<findAllChildBoardComment.size(); i++){
            assertThat(findAllChildBoardComment.get(i).getParentCommentSeq()).isEqualTo(1);
            assertThat(findAllChildBoardComment.get(i).getUser().getUserId()).isEqualTo("user2");
        }
    }

}
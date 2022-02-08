package com.work.daily.init;

import com.work.daily.board.dto.BoardCommentDto;
import com.work.daily.board.dto.BoardDto;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardCommentRepository;
import com.work.daily.domain.repository.BoardRepository;
import com.work.daily.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
//@Order
@Configuration
@Component("DbBoardInsert")
public class DbBoardInsert {

    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final UserRepository userRepository;

    /**
     * @filename : DbBoardInsert.java
     * @description : 게시판 등록 (각 3번이 게시판 등록)
     * @author : 이준범
     */
    @Bean("boardInfoInsert")
    @DependsOn(value = {"userInfoInsert"})
    public void boardInfoInsert() {
        List<Board> boardDtoList = new ArrayList<>();

        List<User> userList = userRepository.findAll().stream()
                .filter(s -> s.getUserId().contains("User3"))
                .collect(Collectors.toList());

        for (int i = 0; i < userList.size(); i++) {
            BoardDto boardDto = new BoardDto();
            boardDto.setUser(User.builder()
                    .userSeq(userList.get(i).getUserSeq())
                    .userId(userList.get(i).getUserId())
                    .userNm(userList.get(i).getUserNm())
                    .build()
            );
            boardDto.setBoardNm(userList.get(i).getUserNm() + " 의 문의사항");
            boardDto.setTemporaryYn("N");
            boardDto.setDelYn("N");
            boardDto.setBoardType("test");
            boardDto.setBoardDesc("test");

            boardDtoList.add(boardDto.toEntity());
        }

        boardRepository.saveAll(boardDtoList);

    }


    /**
     * @filename : DbBoardInsert.java
     * @description : 게시판 코멘트 등록 (각 4번, 5번이 게시판 코멘트 등록)
     * @author : 이준범
     */
    @Bean("boardCommentInsert")
    @DependsOn(value = {"boardInfoInsert"})
    public void boardCommentInsert() {
        List<BoardComment> boardCommentList = new ArrayList<>();

        List<Board> boardList = boardRepository.findAll();

        for (int i = 0; i < boardList.size(); i++) {
            String userProvider = boardList.get(i).getUser().getUserId().substring(0, boardList.get(i).getUser().getUserId().indexOf("User3"));

            for (int j = 4; j < 6; j++) {
                BoardCommentDto boardCommentDto = new BoardCommentDto();
                String userId = userProvider + "User" + j;

                Optional<User> byUserId = userRepository.findByUserId(userId);
                Optional<Board> byUserSeq = boardRepository.findByUser(boardList.get(i).getUser());

                boardCommentDto.setUser(User.builder()
                        .userSeq(byUserId.get().getUserSeq())
                        .userId(userId)
                        .build());
                boardCommentDto.setBoard(Board.builder()
                        .boardSeq(byUserSeq.get().getBoardSeq())
                        .build());
                boardCommentDto.setDelYn("N");
                boardCommentDto.setCommentDesc(userId+"가 댓글 테스트합니다");

                boardCommentList.add(boardCommentDto.toEntity());
            }
        }

        boardCommentRepository.saveAll(boardCommentList);

    }


}

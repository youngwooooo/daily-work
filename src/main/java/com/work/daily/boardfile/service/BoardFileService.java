package com.work.daily.boardfile.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.boardfile.dto.BoardFileDto;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardFile;
import com.work.daily.domain.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardFileService {

    private final BoardFileRepository boardFileRepository;

    @Value("${custom.path.board-file}")
    private String boardFileUploadPath;

    /**
     * 게시글 파일 업로드
     * @param files
     * @param board
     * @return
     * @throws IOException
     */
    @Transactional
    public String save(List<MultipartFile> files, Board board) throws IOException {

        List<BoardFile> boardFiles = new ArrayList<>();

        File uploadFolder = new File(boardFileUploadPath + board.getBoardSeq());
        if(!uploadFolder.exists()){
            uploadFolder.mkdirs();
        }

        for(int i=0; i<files.size(); i++){
            String fileServerNm = URLEncoder.encode(UUID.randomUUID() + "_" + files.get(i).getOriginalFilename(), "UTF-8");

            File boardFile = new File(uploadFolder, fileServerNm);
            files.get(i).transferTo(boardFile);

            String fileUploadPath = "/boardfile/" + board.getBoardSeq() + "/" + fileServerNm;

            BoardFileDto boardFileDto = null;

            if(files.get(i).getContentType().contains("image")){
                boardFileDto = BoardFileDto.builder()
                                    .board(board)
                                    .fileOriginNm(files.get(i).getOriginalFilename())
                                    .fileServerNm(fileServerNm)
                                    .fileUploadPath(fileUploadPath)
                                    .fileSize(files.get(i).getSize())
                                    .imageYn("Y")
                                    .insDtm(LocalDateTime.now())
                                    .build();

            }else {
                boardFileDto = BoardFileDto.builder()
                                    .board(board)
                                    .fileOriginNm(files.get(i).getOriginalFilename())
                                    .fileServerNm(fileServerNm)
                                    .fileUploadPath(fileUploadPath)
                                    .fileSize(files.get(i).getSize())
                                    .imageYn("N")
                                    .insDtm(LocalDateTime.now())
                                    .build();
            }

            boardFiles.add(boardFileDto.toEntity());
        }

        boardFileRepository.saveAll(boardFiles);

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 게시글 파일 조회
     * @param fileSeq
     * @return
     */
    @Transactional(readOnly = true)
    public BoardFileDto findOneBoardFile(long fileSeq){
        Optional<BoardFile> findOneBoardFile = boardFileRepository.findById(fileSeq);
        if(!findOneBoardFile.isPresent()){
            throw new IllegalArgumentException("해당 파일이 존재하지 않습니다. 게시글파일번호 : " + fileSeq);
        }

        BoardFileDto boardFileDto = BoardFileDto.builder()
                                        .fileSeq(findOneBoardFile.get().getFileSeq())
                                        .board(findOneBoardFile.get().getBoard())
                                        .fileOriginNm(findOneBoardFile.get().getFileOriginNm())
                                        .fileServerNm(findOneBoardFile.get().getFileServerNm())
                                        .fileUploadPath(findOneBoardFile.get().getFileUploadPath())
                                        .fileSize(findOneBoardFile.get().getFileSize())
                                        .imageYn(findOneBoardFile.get().getImageYn())
                                        .insDtm(findOneBoardFile.get().getInsDtm())
                                        .build();
        return boardFileDto;
    }

    /**
     * 게시글 파일 삭제
     * @param fileSeqList
     */
    public void delete(List<Long> fileSeqList) {

        for(int i=0; i<fileSeqList.size(); i++){
            Optional<BoardFile> findBoardFile = boardFileRepository.findById(fileSeqList.get(i));

            File file = new File(boardFileUploadPath + findBoardFile.get().getBoard().getBoardSeq() + "/" + findBoardFile.get().getFileServerNm());
            if(file.exists()){
                file.delete();
            }
        }

        boardFileRepository.deleteAllByIdInBatch(fileSeqList);

    }
}

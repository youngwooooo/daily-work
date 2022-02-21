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
                                    .fileOrder(i+1)
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
                                    .fileOrder(i+1)
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
}

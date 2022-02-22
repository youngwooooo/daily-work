package com.work.daily.apiserver.boardfile;

import com.work.daily.boardfile.dto.BoardFileDto;
import com.work.daily.boardfile.service.BoardFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardFileApiController {

    private final ResourceLoader resourceLoader;
    private final BoardFileService boardFileService;

    @Value("${custom.path.board-file}")
    private String boardFileUploadPath;

    /**
     * 게시글 첨부파일 다운로드
     * @param fileSeq
     * @return
     * @throws IOException
     */
    @GetMapping("/boardFile/download/{fileSeq}")
    public ResponseEntity<Resource> boardFileDownload(@PathVariable long fileSeq) throws IOException {

        // 파일 조회
        BoardFileDto findOneBoardFile = boardFileService.findOneBoardFile(fileSeq);

        // 파일 객체 생성
        File file = new File(boardFileUploadPath + findOneBoardFile.getBoard().getBoardSeq() + "/" + findOneBoardFile.getFileServerNm());
        if(!file.exists()){
            throw new FileNotFoundException("해당 파일이 존재하지 않습니다.");
        }

        // 파일 객체에서 mediaType 구하기
        String mediaType = new Tika().detect(file);
        if(mediaType == null){
            mediaType = "application/octet-stream";
        }

        // 파일 객체 절대 경로
        Path filePath = Paths.get(file.getAbsolutePath());

        // 다운로드 될 파일 읽어오기
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=\"" + URLEncoder.encode(findOneBoardFile.getFileOriginNm(), "UTF-8") + "\"")  // 다운받을 파일이름
                .header(HttpHeaders.CONTENT_TYPE, mediaType) // 다운받을 파일 mediaType
                .header(HttpHeaders.CONTENT_LENGTH, file.length() + "") // 다운받을 파일 사이즈
                .body(resource); // 다운받을 파일
    }

}

$(function(){

    // 툴팁 활성화
    $('[data-toggle="tooltip"]').tooltip()

    // 파일 용량 포맷
    $(".file-size").each(function(){
        $(this).text("(" + fileSizePrint($(this).text()) + ")");
    });

    // 파일 다운로드
    $(".board-file-list .list-group-item .file-download").on("click", function(){
        var fileSeq = $(this).siblings("input[name='fileSeq']").val();
        location.href = "/boardFile/download/" + fileSeq;
    });

});

function fileSizePrint(fileSize){
     var result = "";
     if(fileSize < 1024){
        result = fileSize + "B";
     }else if(fileSize < 1024 * 1024) {
        result = parseInt (fileSize / 1024) + "KB";
     }else if(fileSize < 1024 * 1024 * 1024) {
        result = parseInt (fileSize / (1024 * 1024)) + "M";
     }else {
        result = parseInt (fileSize / (1024 * 1024 * 1024)) + "G";
     }

     return result;
}
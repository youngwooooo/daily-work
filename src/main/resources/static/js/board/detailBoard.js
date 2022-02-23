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

    // textarea 높이 자동 조절
    autosize($(".board-comment-content"));

    // textarea 글자 수 제한하기
    $(document).on('keydown keyup', ".board-comment-content" , function () {
        var content = $(this).val(); // 글자수

        $(".text-count").text(content.length);
        $(".text-count-div").css("display", "block");

    });

    $(".board-comment-create-reply").on("click", function(){
        var userNm = $("#userNm").val();

        var str = '<li class="board-comment-reply">'
                +  '<div class="create-board-comment-form-div">'
                +       '<div class="create-board-comment-form justify-content-between">'
                +           '<span>'+ userNm + '</span>'
                +           '<div class="text-count-div">'
                +               '<span class="text-count"></span>/<span>200</span>'
                +           '</div>'
                +       '</div>'
                +       '<textarea class="board-comment-content" placeholder="답글을 입력하세요" maxlength="200"></textarea>'
                +       '<div class="board-comment-buttons">'
                +           '<button type="button" class="btn btn-primary">등록</button>'
                +       '</div>'
                +   '</div>'
                + '</li>';

        if($(".board-comment-reply").length > 0){
            $(".board-comment-reply").remove();
        }else {
            $(this).parents(".board-comment").after(str);
        }


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
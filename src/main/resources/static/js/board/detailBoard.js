$(function(){

    /* 게시글 관련 js */

    // 게시글 수정, 삭제 툴팁 활성화
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

    // 게시글 - [수정]
    $("#btn-board-edit-form").on("click", function(){
        var boardSeq = $("#boardSeq").val();
        location.href = "/board/" + boardSeq + "/modify";
    });

    // 게시글 - [삭제] - 경고 modal
    $("#btn-board-delete-modal").on("click", function(){
        $("#response-modal").show();
    });

    // 게시글 - [삭제] - 경고 modal - [취소]
    $("#btn-cancel-response-modal").on("click", function(){
        $("#response-modal").hide();
    });

    // 게시글 - [삭제] - 경고 modal - [삭제]
    $("#btn-board-delete").on("click", function(){
        var boardSeq = $("#boardSeq").val();

        $.ajax({
            url : "/board/" + boardSeq
            , type : "delete"
            , dataType: "json"
            , success : function(result){
                console.log(result);
                if(result.status == 200){
                    location.href = "/boards";
                }else {
                    alert(result.message);
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    // 댓글 textarea 높이 자동 조절
    autosize($(".board-comment-content"));

    // textarea 글자 수 제한하기
    $(document).on('keydown keyup', ".board-comment-content" , function () {
        var content = $(this).val(); // 글자수

        $(".text-count").text(content.length);
        $(".text-count-div").css("display", "block");

    });

    // 댓글 - [등록]
    $("#btn-create-board-comment").on("click", function(){
        var commentDesc = $(".create-board-comment-form-div .board-comment-content").val();
        var parentCommentSeq = 0;

        if(validationBoardComment(commentDesc)){
            createBoardComment(parentCommentSeq, commentDesc);
        }
    });

    // 댓글 - 수정 form
    $(document).on("click", ".modify-board-comment-form", function(){
        if($("#modify-board-comment-form").length > 0){
            $("#modify-board-comment-form").siblings(".board-comment-info-div").css("display", "block");
            $("#modify-board-comment-form").remove();
        }

        $(this).parents(".board-comment-info-div").css("display", "none");

        var userNm = $("#userNm").val();
        var commentDesc = $(this).parents(".board-comment-info-div").find(".board-comment-desc").text();
        var str = '<div class="create-board-comment-form-div" id="modify-board-comment-form">'
                +       '<div class="create-board-comment-form justify-content-between">'
                +           '<span>' + userNm + '</span>'
                +           '<div class="text-count-div">'
                +               '<span class="text-count"></span>/<span>200</span>'
                +           '</div>'
                +       '</div>'
                +       '<textarea class="board-comment-content" placeholder="댓글을 입력하세요" maxlength="200">'+ commentDesc +'</textarea>'
                +       '<div class="board-comment-buttons">'
                +           '<button type="button" class="btn btn-primary" id="btn-modify-board-comment">수정</button>'
                +           '<button type="button" class="btn btn-light" id="btn-cancel-modify-board-comment">취소</button>'
                +       '</div>'
                +   '</div>';

        $(this).parents(".board-comment").prepend(str);
        autosize($(".board-comment-content"));

    });

    // 댓글 - 수정 form - [취소]
    $(document).on("click", "#btn-cancel-modify-board-comment", function(){
        $("#modify-board-comment-form").siblings(".board-comment-info-div").css("display", "block");
        $(this).parents("#modify-board-comment-form").remove();
    });

    // 댓글 - 수정 form - [수정]
    $(document).on("click", "#btn-modify-board-comment", function(){
        var commentSeq = $(this).parents(".board-comment").find("input[name='commentSeq']").val();
        var commentDesc = $(this).parent().siblings(".board-comment-content").val();

        if(validationBoardComment(commentDesc)){
            modifyBoardComment(commentSeq, commentDesc);
        }
    });

    // 댓글 - [삭제]
    $(document).on("click", ".delete-board-comment", function(){
        var commentSeq = $(this).parents(".board-comment").find("input[name='commentSeq']").val();
        deleteBoardComment(commentSeq);
    });


    // 답글쓰기
    $(document).on("click", ".board-comment-create-reply", function(){
        var userNm = $("#userNm").val();
        var parentCommentSeq = $(this).parents(".board-comment").find("input[name='commentSeq']").val();

        var str = '<li class="board-comment-reply-form">'
                +  '<input type="hidden" name="parentCommentSeq" value="'+ parentCommentSeq +'">'
                +  '<div class="create-board-comment-form-div">'
                +       '<div class="create-board-comment-form justify-content-between">'
                +           '<span>'+ userNm + '</span>'
                +           '<div class="text-count-div">'
                +               '<span class="text-count"></span>/<span>200</span>'
                +           '</div>'
                +       '</div>'
                +       '<textarea class="board-comment-content" placeholder="답글을 입력하세요" maxlength="200"></textarea>'
                +       '<div class="board-comment-buttons">'
                +           '<button type="button" class="btn btn-primary" id="btn-create-comment-reply">등록</button>'
                +           '<button type="button" class="btn btn-light" id="btn-cancel-comment-reply">취소</button>'
                +       '</div>'
                +   '</div>'
                + '</li>';

        if($(".board-comment-reply-form").length > 0){
            $(".board-comment-reply-form").remove();
            $(this).parents(".board-comment").after(str);
            autosize($(".board-comment-content"));
        }else {
            $(this).parents(".board-comment").after(str);
            autosize($(".board-comment-content"));
        }

    });

    // 답글쓰기 - [등록]
    $(document).on("click", "#btn-create-comment-reply", function(){
        var commentDesc = $(".board-comment-reply-form .board-comment-content").val();
        var parentCommentSeq = $(this).parents(".board-comment-reply-form").find("input[name='parentCommentSeq']").val();

        if(validationBoardComment(commentDesc)){
            createBoardComment(parentCommentSeq, commentDesc);
        }
    });

    // 답글쓰기 - [취소]
    $(document).on("click", "#btn-cancel-comment-reply", function(){
       $(this).parents(".board-comment-reply-form").remove();
    });

});

function validationBoardComment(commentDesc){
    if(commentDesc == "" || commentDesc == null){
        alert("댓글(답글)을 입력해주세요.");
        return false;
    }

    return true;
}

function createBoardComment(parentCommentSeq, commentDesc){
    var boardSeq = $("#boardSeq").val();
    var userSeq = $("#userSeq").val();
    var userId = $("#userId").val();
    var delYn = "N";

    var data = {
        "board" : {
            "boardSeq" : boardSeq
        }
        , "user" : {
            "userSeq" : userSeq
            , "userId" : userId
        }
        , "commentDesc" : commentDesc
        , "delYn" : delYn
        , "parentCommentSeq" : parentCommentSeq

    };

    $.ajax({
        url : "/board/comment"
        , type : "post"
        , data : JSON.stringify(data)
        , contentType: "application/json; charset=UTF-8"
        , dataType : "json"
        , success : function(result){
            if(result.status == 201){
                $(".board-comment-content").val("");
                $(".text-count").text(0);
                $("#board-comment-list-div").load(window.location.href + " #board-comment-list-div");
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}

function modifyBoardComment(commentSeq, commentDesc){
    var data = {
        "commentSeq" : commentSeq
        , "commentDesc" : commentDesc
    };

    $.ajax({
        url : "/board/comment/" + commentSeq
        , type : "patch"
        , data : JSON.stringify(data)
        , contentType: "application/json; charset=UTF-8"
        , dataType : "json"
        , success : function(result){
            if(result.status == 200){
                $("#board-comment-list-div").load(window.location.href + " #board-comment-list-div");
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}

function deleteBoardComment(commentSeq){
    $.ajax({
        url : "/board/comment/" + commentSeq
        , type : "delete"
        , dataType : "json"
        , success : function(result){
            console.log(result);
            if(result.status == 200){
                $("#board-comment-list-div").load(window.location.href + " #board-comment-list-div");
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}

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
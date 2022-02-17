$(function(){

    CKEDITOR.replace("boardDesc", {
        height: 400
    });

    $("#btn-save-board").on("click", function(){
        alert("글 등록");
    });

    $("#btn-temporary-board").on("click", function(){
        alert("글 임시저장");
    });

    $("#btn-cancel").on("click", function(){
        location.href = "/boards";
    });

});

// 글 쓰기 - 유효성 검사
function validatingBoard(){
    var boardNm = $("#boardNm").val();
    var boardDesc = CKEDITOR.instances.boardDesc.getData();

    if(boardNm == "" || boardNm == null){
        alert("게시글 제목을 입력해주세요.");
        return false;
    }

    if(boardDesc == "" || boardDesc == null){
        alert("게시글 내용을 입력해주세요.");
        return false;
    }

    return true;
}

/*
function savingBoard(temporaryValue){
    var userSeq = $("#userSeq").val();
    var userId = $("#userId").val();
    var boardNm = $("#boardNm").val();  // 미션명
    var boardDesc = CKEDITOR.instances.boardDesc.getData();  // 미션 설명
    var delYn = "N";    // 삭제 여부
    var temporaryYn = temporaryValue   // 임시 여부

    var data = {
                    "user" : {
                        "userSeq" : userSeq
                        , "userId" : userId
                    }
                    , "boardNm" : boardNm
                    , "boardDesc" : boardDesc
                    , "delYn" : delYn
                    , "temporaryYn" : temporaryYn
                };

    var formData = new FormData();
    formData.append("file", boardImage);
    formData.append("", new Blob([JSON.stringify(data)] , {type: "application/json"}));

    $.ajax({
        url : "/board"
        , type : "post"
        , data : formData
        , processData: false
        , contentType: false
        , enctype: "multipart/form-data"
        , success : function(result){
            if(result.status == 201){
                alert(result.message);
                location.href = "/boards";
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}*/

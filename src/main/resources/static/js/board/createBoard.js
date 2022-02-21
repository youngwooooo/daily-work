$(function(){

    // 첨부파일 객체를 담을 array 전역 변수
    var fileArr = new Array();

    CKEDITOR.replace("boardDesc", {
        height: 400
    });

    // [파일 선텍]
    $("#select-file").on("click", function(){
        if(fileArr.length > 2){
            alert("파일은 최대 3개까지 첨부 가능합니다\n(파일을 먼저 삭제 해주세요.)");
            return;
        }else {
            $("#file").click();
        }
    });

    // [파일 선택] - 파일 첨부
    $("#file").on("change", function(){
        var files = $(this)[0].files;

        if(files.length > 3){
            alert("파일은 최대 3개까지 첨부 가능합니다.");
            $(this).val("");
            return;
        }

        for(var i=0; i<files.length; i++){
            fileArr.push(files[i]);
            var fileList = '<il class="list-group-item">'
                    + '<span class="file-name">'+ files[i].name + '</span>'
                    + '<span>(' + fileSizePrint(files[i].size) + ')</span>'
                    + '<span class="delete-file"><i class="fa-solid fa-xmark"></i></span>'
                    + '</il>';

            $(".preview-file-list ul").append(fileList);
        }

        $(".preview-file-wrapper").css("display", "flex");

    });

    // 첨부 파일 삭제
    $(document).on("click", ".delete-file", function(){
        var fileName = $(this).siblings(".file-name").text();
        fileArr.forEach(function(item){
            if(fileName == item.name){
                var idx = fileArr.indexOf(item);
                fileArr.splice(idx, 1);
            }
        });

        $(this).parent().remove();

        if($(".preview-file-list .list-group").children().length == 0){
            $(".preview-file-wrapper").css("display", "none");
        }

    });

    // [등록]
    $("#btn-save-board").on("click", function(){
        if(validatingBoard()){
            savingBoard("N", fileArr);
        }
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

function savingBoard(temporaryValue, fileArr){
    var userSeq = $("#userSeq").val();
    var userId = $("#userId").val();
    var boardNm = $("#boardNm").val();
    var boardDesc = CKEDITOR.instances.boardDesc.getData();
    var delYn = "N";
    var temporaryYn = temporaryValue
    var boardType = $(".table select").val();

    var data = {
                    "user" : {
                        "userSeq" : userSeq
                        , "userId" : userId
                    }
                    , "boardNm" : boardNm
                    , "boardDesc" : boardDesc
                    , "delYn" : delYn
                    , "temporaryYn" : temporaryYn
                    , "boardType" : boardType
                };

    var formData = new FormData();

    fileArr.forEach(function(item){
        formData.append("files", item);
    });
    formData.append("requestBoardDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

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
            }else {
                alert(result.message);
                return;
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

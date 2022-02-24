$(function(){

    // 첨부 파일 객체를 담을 전역 변수
    var fileArr = new Array();
    // 삭제된 파일 번호를 담을 전역 변수
    var fileSeqList = new Array();
    // 첨부 파일 총 용량을 담을 전역 변수
    var totalFileSize = 0;
    // 첨부 파일 용량 세팅
    $("input[name='fileSize']").each(function(idx, item){
        totalFileSize += parseInt(item.value);
    });
    $(".file-size-view").text(fileSizePrint(totalFileSize));
    $(".board-file-select-div > div").css("display", "block");

    CKEDITOR.replace("boardDesc", {
            height: 400
    });

    // [파일 선텍]
    $("#select-file").on("click", function(){
        $("#file").click();
    });

    // 파일 용량 포맷
    $(".file-size").each(function(){
        $(this).text("(" + fileSizePrint($(this).text()) + ")");
    });

    // [파일 선택] - 파일 첨부
    $("#file").on("change", function(){
        var files = $(this)[0].files;
        var maxFileSize = 10 * 1024 * 1000;


        for(var i=0; i<files.length; i++){
            // 개당 첨부 파일 용량 제한
            if(files[i].size > maxFileSize){
                alert("첨부 파일의 최대 용량는 10MB 입니다");
                return;
            }

            // 파일 용량 누적
            totalFileSize += files[i].size;
            $(".file-size-view").text(fileSizePrint(totalFileSize));

            // 총 첨부 파일 용량 제한
            if(totalFileSize > maxFileSize){
                alert("첨부 파일의 최대 용량는 10MB 입니다.");
                return;
            }

            fileArr.push(files[i]);
            var fileList = '<il class="list-group-item">'
                    + '<span><i class="fa-solid fa-folder-open fa-lg mr-3"></i></span>'
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
                totalFileSize = totalFileSize - parseInt(item.size);
                $(".file-size-view").text(fileSizePrint(totalFileSize));

                var idx = fileArr.indexOf(item);
                fileArr.splice(idx, 1);
            }
        });

        $(this).parent().remove();

        var fileSize = $(this).siblings("input[name='fileSize']").val();
        if(fileSize != undefined){
            totalFileSize = totalFileSize - parseInt(fileSize);
            $(".file-size-view").text(fileSizePrint(totalFileSize));
        }

        var fileSeq = $(this).siblings("input[name='fileSeq']").val();
        if(fileSeq != undefined){
            fileSeqList.push(parseInt(fileSeq));
        }

        if($(".preview-file-list .list-group").children().length == 0){
            $(".preview-file-wrapper").css("display", "none");
        }

    });

    // [등록]
    $("#btn-save-board").on("click", function(){
        if(validatingBoard()){
            modifyBoard(fileArr, fileSeqList);
        }

    });

});

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

function modifyBoard(fileArr, fileSeqList){
    var boardSeq = $("#boardSeq").val();
    var boardNm = $("#boardNm").val();
    var boardDesc = CKEDITOR.instances.boardDesc.getData();
    var boardType = $(".table select").val();

    var data = {
                     "boardNm" : boardNm
                    , "boardDesc" : boardDesc
                    , "boardType" : boardType
                };

    var formData = new FormData();

    fileArr.forEach(function(item){
        formData.append("files", item);
    });
    formData.append("requestBoardDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));
    formData.append("fileSeqList", new Blob([JSON.stringify(fileSeqList)] , {type: "application/json"}));

    $.ajax({
        url : "/board/" + boardSeq
        , type : "patch"
        , data : formData
        , processData: false
        , contentType: false
        , enctype: "multipart/form-data"
        , success : function(result){
            if(result.status == 201){
                alert(result.message);
            }else {
                alert(result.message);
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
        result = parseInt (fileSize / (1024 * 1024)) + "MB";
     }else {
        result = parseInt (fileSize / (1024 * 1024 * 1024)) + "GB";
     }

     return result;
}
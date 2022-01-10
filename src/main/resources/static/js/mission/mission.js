$(function(){
    /* 전체 MISSION */
    // 전체 MISSION - [미션 만들기] 클릭 시
    $("#btn-create-mission").on("click", function(){
       location.href = "/mission";
    });

    /* 미션 만들기 */
    // 미션시작일 현재 시간으로 세팅(yyyy-MM-dd HH:mm)
    $("#missionStDt").val(new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16));

    // 미션 설명(textarea) CKEditor4 적용
    CKEDITOR.replace("missionDesc", {
        height: 400
    });

    // 대표 이미지 div 클릭 시
    $(".mission-image-div").on("click", function(e){
        $('#file').click();
    });

    // 대표 이미지 변경 시 미리보기
    $("#file").on("change", function(){
        var changeImg = $(this)[0].files[0];

        var reader = new FileReader();
        reader.onload = function (e) {
           if(changeImg.type.match("image/*")){
             $('#mission-image').attr("src", e.target.result);
             $('#mission-image').css("display", "block");
           }else {
             alert("이미지 형식의 파일만 가능합니다.");
           }

        }
        reader.readAsDataURL(changeImg);
    });

    // 미션 만들기 - [등록] 클릭 시
    $("#btn-save-mission").on("click", function(){
        if(validatingMission()){
            // 임시여부 N
            savingMission("N");
        }
    });

    // 미션 만들기 - [임시저장] 클릭 시
    $("#btn-temporary-mission").on("click", function(){
        if(validatingMission()){
            // 임시여부 N
            savingMission("Y");
        }
    });

    // 미션 만들기 - [취소] 클릭 시
    $("#btn-cancel").on("click", function(){
        location.href = "/missions";
    });

});

// 미션 만들기 - 유효성 검사
function validatingMission(){
    var missionNm = $("#missionNm").val();  // 미션명
    var missionDesc = CKEDITOR.instances.missionDesc.getData();  // 미션 설명
    var missionStDt = $("#missionStDt").val();   // 미션 시작일
    var missionEndDt = $("#missionEndDt").val(); // 미션 종료일

    if(missionNm == "" || missionNm == null){
        alert("미션 제목을 입력해주세요.");
        return false;
    }

    if(missionDesc == "" || missionDesc == null){
        alert("미션에 대한 소개 또는 설명을 입력해주세요.");
        return false;
    }

    if(missionEndDt == "" || missionEndDt == null){
        alert("미션 종료일을 선택해주세요.");
        return false;
    }

    var missionStDtForCompare = new Date(missionStDt);
    var missionEndDtForCompare = new Date(missionEndDt);

    if(missionEndDtForCompare <= missionStDtForCompare){
        alert("미션 종료일을 확인해주세요.");
        return false;
    }

    return true;
}

// 미션 만들기 - 미션 생성 로직
function savingMission(temporaryValue){
    var userSeq = $("#userSeq").val();
    var userId = $("#userId").val();
    var missionNm = $("#missionNm").val();  // 미션명
    var missionDesc = CKEDITOR.instances.missionDesc.getData();  // 미션 설명
    var missionImage = $("#file")[0].files[0];  // 대표 이미지
    var missionStDt = $("#missionStDt").val();   // 미션 시작일
    var missionEndDt = $("#missionEndDt").val();    // 미션 종료일
    var releaseYn = $("input[name='releaseYn']").val()  // 공개 여부
    var autoAccessYn = $("input[name='autoAccessYn']").val();   // 자동참여 여부
    var masterYn = "Y"; // 방장 여부
    var delYn = "N";    // 삭제 여부
    var temporaryYn = temporaryValue   // 임시 여부

    var data = {
                    "user" : {
                        "userSeq" : userSeq
                        , "userId" : userId
                    }
                    , "missionNm" : missionNm
                    , "missionDesc" : missionDesc
                    , "missionStDt" : missionStDt
                    , "missionEndDt" : missionEndDt
                    , "releaseYn" : releaseYn
                    , "autoAccessYn" : autoAccessYn
                    , "masterYn" : masterYn
                    , "delYn" : delYn
                    , "temporaryYn" : temporaryYn
                };

    var formData = new FormData();
    formData.append("file", missionImage);
    formData.append("requestMissionDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

    $.ajax({
        url : "/mission"
        , type : "post"
        , data : formData
        , processData: false
        , contentType: false
        , enctype: "multipart/form-data"
        , success : function(result){
            console.log(result);
            if(result.status == 200){
                alert(result.message);
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}
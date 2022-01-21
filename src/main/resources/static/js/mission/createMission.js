$(function(){
    /* 미션 만들기 */

    /* 미션시작일, 미션종료일 설정 */
    // 현재날짜
    var now = new Date();
    // 현재날짜 포맷팅
    var nowFormat = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    // 1. 미션시작일 현재 날짜로 설정
    $("#missionStDt").val(nowFormat);
    // 2. 미션종료일을 현재날짜 보다 이전을 선택할 수 없게 함
    $("#missionEndDt").attr("min", nowFormat);
    // 3. 미션종료일은 미션시작일로부터 최대 한달로 설정
    // 한달 후 날짜 포맷팅
    var oneMonthLaterFormat = new Date(Date.parse(now) + 30 * 1000 * 60 * 60 * 24).toISOString().slice(0, 16);
    $("#missionEndDt").attr("max", oneMonthLaterFormat);

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

    var missionDateDifference = new Date(missionEndDt) - new Date(missionStDt);
    if(missionDateDifference <  "86400000"){
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
    var releaseYn = $("input[name='releaseYn']:checked").val()  // 공개 여부
    var autoAccessYn = $("input[name='autoAccessYn']:checked").val();   // 자동참여 여부
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
            if(result.status == 201){
                alert(result.message);
                location.href = "/missions";
            }
        }
        , error : function(xhr){
            console.log(xhr);
        }
    });
}
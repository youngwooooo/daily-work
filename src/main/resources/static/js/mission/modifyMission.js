/* 미션 수정 페이지 js */
$(function(){
    // 미션 설명(textarea) CKEditor4 적용
    CKEDITOR.replace("missionDesc", {
        height: 400
    });
    // 미션 설명 데이터 넣기
    CKEDITOR.instances.missionDesc.setData($("#find-missionDesc").val());

    $("#mission-image").css("display", "inline");

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

    // 공개여부 Y/N 설정
    if($("#find-releaseYn").val() == "Y"){
        $("#releaseY").attr("checked", true);
        $("#releaseN").attr("checked", false);
    }else {
        $("#releaseN").attr("checked", true);
        $("#releaseY").attr("checked", false);
    }
    // 자동참여 여부 Y/N 설정
    if($("#find-autoAccessYn").val() == "Y"){
        $("#autoAccessY").attr("checked", true);
        $("#autoAccessN").attr("checked", false);
    }else {
        $("#autoAccessN").attr("checked", true);
        $("#autoAccessY").attr("checked", false);
    }

    // 미션 수정 페이지 - [수정]
    $("#btn-modify-mission").on("click", function(){
        if(validatingMission()){
            var missionSeq = $("#missionSeq").val();
            var userSeq = $("#userSeq").val();
            var userId = $("#userId").val();
            var missionNm = $("#missionNm").val();  // 미션명
            var missionDesc = CKEDITOR.instances.missionDesc.getData();  // 미션 설명
            var missionStDt = $("#missionStDt").val();   // 미션 시작일
            var missionEndDt = $("#missionEndDt").val();    // 미션 종료일
            var releaseYn = $("input[name='releaseYn']:checked").val()  // 공개 여부
            var autoAccessYn = $("input[name='autoAccessYn']:checked").val();   // 자동참여 여부
            var masterYn = $("#find-masterYn").val(); // 방장 여부
            var delYn = $("#find-delYn").val();    // 삭제 여부
            var temporaryYn = $("#find-temporaryYn").val();

            var missionImage = $("#file")[0].files[0];  // 대표 이미지
            var data = null;
            // 이미지가 존재하지 않을 때
            if(missionImage == null){
                missionImage = $("#find-missionImage").val();
                data = {
                            "missionSeq" : missionSeq
                            , "user" : {
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
                            , "missionImage" : missionImage
                        };
            }else {
                data = {
                            "missionSeq" : missionSeq
                            , "user" : {
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
            }

            var formData = new FormData();
            formData.append("file", missionImage);
            formData.append("requestMissionDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

            $.ajax({
                url : "/mission/" + missionSeq
                , type : "patch"
                , data : formData
                , processData: false
                , contentType: false
                , enctype: "multipart/form-data"
                , success : function(result){
                    if(result.status == 200){
                        alert(result.message);
                        location.href = "/mission/" + missionSeq;
                    }
                }
                , error : function(xhr){
                    console.log(xhr);
                }
            });

        }

    });

});

// 미션 수정  - 유효성 검사
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
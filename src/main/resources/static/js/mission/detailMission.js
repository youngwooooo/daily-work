$(function(){
    /* 미션 상세 조회 */
    // 미션 시작일, 미션 종료일 날짜 포맷팅
    var formatMissionStDt = $(".mission-start-date").text().replace("T", " ");
    var formatMissionEndDt = $(".mission-end-date").text().replace("T", " ");
    $(".mission-start-date").text(formatMissionStDt);
    $(".mission-end-date").text(formatMissionEndDt);

    // 미션 참여자 존재 여부에 따른 [미션 참여하기], [미션 탈퇴하기] 버튼 보이기
    // 미션 생성자와 로그인 한 회원이 같을 경우
    if($("#missionCreatedUserId").val() == $("#userId").val()){
        $("#btn-modify-mission").css("display", "inline-block");
        $("#btn-delete-mission").css("display", "inline-block");
    }else {

        var participantsIdInput = $("input[name='participantsId']");
        for(var i=0; i<participantsIdInput.length; i++){
            // 미션 참여자
            var participantsId = participantsIdInput[i].value;
            // 미션 참여자가 로그인 한 회원과 같을 경우
            if(participantsId == $("#userId").val()){
                $("#btn-join-mission").css("display", "none");
                $("#btn-secession-mission").css("display", "inline-block");
            }else {
                $("#btn-join-mission").css("display", "inline-block");
                $("#btn-secession-mission").css("display", "none");
            }
        }
    }

    /* 미션 상세 조회 - [미션 삭제하기] */
    $("#btn-delete-mission").on("click", function(){
        var missionSeq = $("#missionSeq").val();
        var delYn = "Y";

        var data = {
            "missionSeq" : missionSeq
            , "delYn" : delYn
        };

        $.ajax({
            url : "/mission/" + missionSeq
            , type : "delete"
            , data : data
            , contentType: "application/x-www-form-urlencoded; charset=UTF-8;"
            , dataType : "json"
            , success : function(result){
                if(result.status == "201"){
                    alert(result.message);
                    location.href = "/missions";
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });


    /* 미션 상세 조회 - [미션 참여하기] */
    $("#btn-join-mission").on("click", function(){
        if($("#userId").val() == null){
            alert("로그인이 필요합니다.");
            return;
        }

        var missionSeq = $("#missionSeq").val(); // 미션번호
        var userSeq = $("#userSeq").val();       // 회원번호
        var userId = $("#userId").val();         // 회원ID
        var missionJoinDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16); // 미션참여일

        var autoAccessYn = $("#autoAccessYn").val();    // 자동참여 여부
        var missionJoinYn = ""; // 참여여부
        var missionJoinApprovalDt = ""; // 참여승인일
        // 자동참여 여부에 따른 참여여부 값
        if(autoAccessYn == "Y"){
            missionJoinYn = "Y"
            missionJoinApprovalDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
        }else {
            missionJoinYn = "N";
            missionJoinApprovalDt = null;
        }

        var data = {
                        "missionSeq" : missionSeq
                        , "userSeq" : userSeq
                        , "userId" : userId
                        , "missionJoinDt" : missionJoinDt
                        , "missionJoinYn" : missionJoinYn
                        , "missionJoinApprovalDt" : missionJoinApprovalDt
                   };

        $.ajax({
            url: "/mission/" + missionSeq + "/join"
            , type : "post"
            , data : JSON.stringify(data)
            , contentType: "application/json; charset=UTF-8"
            , dataType : "json"
            , success : function(result){
                if(result.status == "201"){
                    alert(result.message);
                    location.reload();
                }
            }
            , error : function(xhr){
                if(xhr.responseJSON.status = "400"){
                    alert("이미 참여한 미션입니다.");
                }
            }
        });

    });

    /* 미션 상세 조회 - [미션 탈퇴하기] */
    $("#btn-secession-mission").on("click", function(){
        var missionSeq = $("#missionSeq").val();
        var userSeq = $("#userSeq").val();
        var userId = $("#userId").val();

        var data = {
                        "missionSeq" : missionSeq
                        , "userSeq" : userSeq
                        , "userId" : userId
                   };

        $.ajax({
            url : "/mission/" + missionSeq + "/secession"
            , type : "delete"
            , data : JSON.stringify(data)
            , contentType: "application/json; charset=UTF-8"
            , dataType : "json"
            , success : function(result){
                if(result.status == "200"){
                    alert(result.message);
                    location.href = "/missions";
                }
            }
            , error : function(xhr){
                if(xhr.responseJSON.status = "400"){

                }
            }
        });

    });

});
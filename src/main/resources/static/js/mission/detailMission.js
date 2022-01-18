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
        $("#btn-modify-mission-form").css("display", "inline-block");
        $("#btn-delete-mission-modal").css("display", "inline-block");
    }else {

        var participantsIdInput = $("input[name='participantsId']");
        for(var i=0; i<participantsIdInput.length; i++){
            // 미션 참여자
            var participantsId = participantsIdInput[i].value;
            // 미션 참여자가 로그인 한 회원과 같을 경우
            if(participantsId == $("#userId").val()){
                $("#btn-join-mission-modal").css("display", "none");
                $("#btn-secession-mission-modal").css("display", "inline-block");
            }else {
                $("#btn-join-mission-modal").css("display", "inline-block");
                $("#btn-secession-mission-modal").css("display", "none");
            }
        }
    }
    /* 모달이 띄워질 때 */
    /* 모달 버튼들을 보이지 않도록 함 */
    $(".response-modal-btn-div").css("display", "none");

    /* 비로그인 사용자 - [미션 참여하기] - [로그인] */
    $("#btn-login-form").on("click", function(){
        location.href = "/login";
    });
    /* 비로그인 사용자 - [미션 참여하기] - [회원가입] */
    $("#btn-join-form").on("click", function(){
        location.href = "/join";
    });
    /* 비로그인 사용자 - [미션 참여하기] - [홈으로] */
    $("#btn-main-form").on("click", function(){
        location.href = "/missions";
    });

    /* [취소](공통) */
    $("button[name='btn-cancel']").on("click", function(){
        $("#response-modal").hide();
    });

    /* 미션 상세 조회 - [미션 수정하기] */
     $("#btn-modify-mission-form").on("click", function(){
        var missionSeq = $("#missionSeq").val();
        location.href = "/mission/" + missionSeq + "/modify";
     });

    /* 미션 상세 조회 - [미션 삭제하기] */
    $("#btn-delete-mission-modal").on("click", function(){
        $("#response-modal .modal-body h3").text("미션을 삭제하시겠습니까?");
        $("#response-modal .modal-body p").text("모든 미션 정보가 삭제됩니다. 삭제된 미션은 복구할 수 없습니다.");
        $("#delete-mission-btn-div").css("display", "block")

        $("#response-modal").show();

    });

    /* 미션 상세 조회 - [미션 삭제하기] - [삭제] */
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
                    location.href = "/missions";
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });


    /* 미션 상세 조회 - [미션 참여하기] */
    $("#btn-join-mission-modal").on("click", function(){
        // 비로그인 사용자가 [미션 참여하기] 클릭 시
        if($("#userId").val() == null){
            $("#response-modal .modal-body h3").text("로그인이 필요해요!");
            $("#response-modal .modal-body p").text("아이디가 없다면 회원가입을 해보세요.");
            $("#no-login-btn-div").css("display", "block")

            $("#response-modal").show();
        }else {
            // 자동참여 여부가 Y 일 때
            if($("#autoAccessYn").val() == "Y"){
                $("#response-modal .modal-body h3").text("미션에 바로 참여할 수 있어요!");
                $("#response-modal .modal-body p").text("미션에 참여해 목표를 달성해볼까요?");
                $("#join-mission-btn-div").css("display", "block")

                $("#response-modal").show();
            }else {
                $("#response-modal .modal-body h3").text("참여 승인이 필요해요!");
                $("#response-modal .modal-body p").text("미션 작성자의 승인 후 미션에 참여할 수 있어요.");
                $("#join-mission-btn-div").css("display", "block")

                $("#response-modal").show();
            }
        }

    });

    /* 미션 상세 조회 - [미션 참여하기] - [참여] */
    $("#btn-join-mission").on("click", function(){
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
    $("#btn-secession-mission-modal").on("click", function(){
        $("#response-modal .modal-body h3").text("정말 미션을 탈퇴하시겠어요?");
        $("#response-modal .modal-body p").text("기존에 제출한 미션들은 복구할 수 없습니다.");
        $("#secession-mission-btn-div").css("display", "block")

        $("#response-modal").show();

    });

    /* 미션 상세 조회 - [미션 탈퇴하기] - [탈퇴] */
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
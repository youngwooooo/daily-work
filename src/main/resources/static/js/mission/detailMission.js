/* 미션 상세 조회 js*/
$(function(){
    // 미션 참여자 존재 여부에 따른 [미션 참여하기], [미션 탈퇴하기] 버튼 보이기
    // 미션 생성자와 로그인 한 회원이 같을 경우
    if($("#missionCreatedUserId").val() == $("#userId").val()){
        $("#btn-modify-mission-form").css("display", "inline-block");
        $("#btn-delete-mission-modal").css("display", "inline-block");
        $("#btn-participants-management-modal").css("display", "inline-block");
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

    /* [미션 참여자 관리] */
    $("#btn-participants-management-modal").on("click", function(){
        $("#mission-participants-management-modal").show();
    });

    /* [미션 참여자 관리] - [승인] */
    $("button[name='btn-approve-participants']").on("click", function(){
        var thisButton = $(this);
        var thisParent = $(this).parent();

        var missionSeq = $("#missionSeq").val();
        var userSeq = $(this).parents(".mission-participants-management-div").find("input[name='participantsSeq']").val();
        var userId = $(this).parents(".mission-participants-management-div").find("input[name='participantsId']").val();
        var missionJoinYn = "Y";
        var missionJoinApprovalDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);

        var data = {
            "missionSeq" : missionSeq
            , "userSeq" : userSeq
            , "userId" : userId
            , "missionJoinYn" : missionJoinYn
            , "missionJoinApprovalDt" : missionJoinApprovalDt
        };

        $.ajax({
            url : "/mission/" + missionSeq + "/approve-participants"
            , type : "patch"
            , data : JSON.stringify(data)
            , contentType: "application/json; charset=UTF-8"
            , dataType : "json"
            , success : function(result){
                console.log(result);
                var createdParticipantsSeq = result.data.user.userSeq;
                var createdParticipantsId = result.data.user.userId;
                var createdParticipantsProfileImage = result.data.user.profileImage;
                var createdParticipantsName = result.data.user.userNm;

                var addData = '<div class="mission-participants-info-result">'
                              + '<input type="hidden" name="participantsSeq" value="' + createdParticipantsSeq + '">'
                              + '<input type="hidden" name="participantsId" value="'+ createdParticipantsId +'">'
                              +     '<img src="' + createdParticipantsProfileImage + '">'
                              +     '<span> ' + createdParticipantsName + '</span>'
                              + '</div>';

                $(".mission-participants-info").append(addData);

                thisParent.append('<button type="button" class="btn btn-danger" name="btn-expulsion-participants">강퇴</button>');
                thisButton.remove();
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    /*
        [미션 참여자 관리] - [강퇴]
        승인 처리 후(ajax success 후), 바로 강퇴 할 수도 있으므로 $(document)를 사용하여 처리할 수 있게함.
    */
    $(document).on("click", "button[name='btn-expulsion-participants']", function(){
        var thisParent = $(this).parents().find(".mission-participants-management-div");
        var participantsInfoResultDiv = $(".mission-participants-info-result");

        var missionSeq = $("#missionSeq").val();
        var userSeq = $(this).parents(".mission-participants-management-div").find("input[name='participantsSeq']").val();
        var userId = $(this).parents(".mission-participants-management-div").find("input[name='participantsId']").val();

        var data = {
                    "missionSeq" : missionSeq
                    , "userSeq" : userSeq
                    , "userId" : userId
                };

        $.ajax({
            url : "/mission/" + missionSeq + "/expulsion-participants"
            , type : "delete"
            , data : JSON.stringify(data)
            , contentType: "application/json; charset=UTF-8"
            , dataType : "json"
            , success : function(result){
                console.log(result);
                var deletedParticipantsId = result.data.user.userId;

                for(var i=0; i<participantsInfoResultDiv.length; i++){
                    if(participantsInfoResultDiv[i].children["participantsId"].value == deletedParticipantsId){
                        participantsInfoResultDiv[i].remove();
                        thisParent.remove();
                    }
                }

            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    /*[미션 참여자 관리] - [X] */
    $("button[name='btn-cancel-mission-participants-management-modal']").on("click", function(){
       location.reload();
    });

    /* 미션 상세 조회 - [미션 수정하기] */
     $("#btn-modify-mission-form").on("click", function(){
        var missionSeq = $("#missionSeq").val();
        location.href = "/mission/" + missionSeq + "/modify";
     });

     /* [미션 삭제하기], [미션 참여하기], [미션 탈퇴하기] - [취소] */
     $("button[name='btn-cancel-response-modal']").on("click", function(){
         $("#response-modal").hide();
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
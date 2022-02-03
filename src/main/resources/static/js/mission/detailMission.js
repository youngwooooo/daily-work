/* 미션 상세 조회 js*/
$(function(){
    /* 미션 권한 관련 함수 */

    // 1. 미션 생성자와 로그인 한 회원이 같을 경우 : [미션 수정하기], [미션 삭제하기], [미션 참여자 관리], [미션 제출] 보이기
    if($("#missionCreatedUserId").val() == $("#userId").val()){
        $("#btn-modify-mission-form").css("display", "inline-block");
        $("#btn-delete-mission-modal").css("display", "inline-block");
        $("#btn-participants-management-modal").css("display", "inline-block");
        $(".mission-submit-btn-div").css("display", "block");
    }else {
    // 2. 미션 생성자와 로그인 한 회원이 다를 경우
        $(".mission-participants-info-result input[name='participantsId']").each(function(){
            // 미션 참여자 ID 중 로그인 한 회원의 ID와 같은 경우 [미션 참여하기] 숨기기, [미션 탈퇴하기], [미션 제출] 보이기
            if($(this).val() == $("#userId").val()){
                $("#btn-join-mission-modal").css("display", "none");
                $("#btn-secession-mission-modal").css("display", "inline-block");
                $(".mission-submit-btn-div").css("display", "block");
                return false;
            }

             $("#btn-join-mission-modal").css("display", "inline-block");
             $("#btn-secession-mission-modal").css("display", "none");

        });

    }

    // 3. 비로그인 사용자일 때, [미션 참여하기] 클릭 시
    // 3-1. [미션 참여하기] - [로그인] 클릭 시, 로그인 페이지로 이동
    $("#btn-login-form").on("click", function(){
        location.href = "/login";
    });
    // 3-2. [미션 참여하기] - [회원가입] 클릭 시, 회원가입 페이지로 이동
    $("#btn-join-form").on("click", function(){
        location.href = "/join";
    });
    // 3-3. [미션 참여하기] - [홈으로] 클릭 시, 메인 페이지로 이동
    $("#btn-main-form").on("click", function(){
        location.href = "/missions";
    });

    /************************************************************************************************/

    /* 미션 생성자 - [미션 참여자 관리] 관련 함수 */

    // [미션 참여자 관리] 클릭 시, 모달 띄우기
    $("#btn-participants-management-modal").on("click", function(){
        $("#mission-participants-management-modal").show();
    });

    // [미션 참여자 관리] - [승인] 클릭 시, 미션 참여자 추가 로직
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

    // [미션 참여자 관리] - [강퇴] 클릭 시, 미션 참여자 강퇴 로직
    // 승인 처리 후(ajax success 후), 바로 강퇴 할 수도 있으므로 $(document)를 사용하여 처리할 수 있게함.
    $(document).on("click", "button[name='btn-expulsion-participants']", function(){
        var thisParent = $(this).parent().parent();
        var participantsInfoResultDiv = $(".mission-participants-info-result");
        var missionStateInfoTr = $(".tab-pane .table tbody tr");

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

                missionStateInfoTr.each(function(){
                    var missionStateUser = $(this).find("input[name='participantsId']").val();
                    if(missionStateUser == userId){
                        $(this).remove();
                    }
                });
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    // [미션 참여자 관리] - [X] 클릭 시, 모달 창이 닫히면서 새로고침
    $("button[name='btn-cancel-mission-participants-management-modal']").on("click", function(){
       location.reload();
    });

    /************************************************************************************************/

    /* 미션 생성자 - [미션 수정하기] 관련 함수 */

    // [미션 수정하기] 클릭 시, 미션 수정 페이지로 이동
     $("#btn-modify-mission-form").on("click", function(){
        var missionSeq = $("#missionSeq").val();
        location.href = "/mission/" + missionSeq + "/modify";
     });

    /************************************************************************************************/

     /*
        미션 생성자 - [미션 삭제하기] / 미션 미참여자(회원) - [미션 참여하기] / 미션 참여자(회원) - [미션 탈퇴하기] 관련 함수
        1, 2 : 공통
        3 : 미션 삭제
        4 : 미션 참여
        5 : 미션 탈퇴
     */

     // 1. 공통 - [미션 삭제하기], [미션 참여하기], [미션 탈퇴하기] 클릭 시 띄워지는 모달 창 버튼들을 기본적으로 모두 보이지 않도록 함
     $(".response-modal-btn-div").css("display", "none");

     // 2. [미션 삭제하기], [미션 참여하기], [미션 탈퇴하기] - [취소] 클릭 시, 모달 숨기기
     $("button[name='btn-cancel-response-modal']").on("click", function(){
         $("#response-modal").hide();
     });

    // 3-1. 미션 생성자 - [미션 삭제하기] 클릭 시, 모달 내용 변경 / 버튼 보이기 / 모달 보이기
    $("#btn-delete-mission-modal").on("click", function(){
        $("#response-modal .modal-body h3").text("미션을 삭제하시겠습니까?");
        $("#response-modal .modal-body p").text("모든 미션 정보가 삭제됩니다. 삭제된 미션은 복구할 수 없습니다.");
        $("#delete-mission-btn-div").css("display", "block")

        $("#response-modal").show();

    });

    // 3-2. 미션 생성자 - [미션 삭제하기] - [삭제] 클릭 시, 미션 삭제여부 = N 수정 로직
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

    // 4-1. 비로그인 사용자 or 미션 미참여자 - [미션 참여하기] 클릭 시
    $("#btn-join-mission-modal").on("click", function(){
        // 비로그인 사용자일 때, [미션 참여하기] 클릭 시, 모달 내용 변경 / 버튼 보이기 / 모달 보이기
        if($("#userId").val() == null){
            $("#response-modal .modal-body h3").text("로그인이 필요해요!");
            $("#response-modal .modal-body p").text("아이디가 없다면 회원가입을 해보세요.");
            $("#no-login-btn-div").css("display", "block")

            $("#response-modal").show();
        }else {
        // 미션 미참여자(회원)일 때
            // 미션의 자동참여 여부가 Y 일 때, 모달 내용 변경 / 버튼 보이기 / 모달 보이기
            if($("#autoAccessYn").val() == "Y"){
                $("#response-modal .modal-body h3").text("미션에 바로 참여할 수 있어요!");
                $("#response-modal .modal-body p").text("미션에 참여해 목표를 달성해볼까요?");
                $("#join-mission-btn-div").css("display", "block")

                $("#response-modal").show();
            }else {
            // 미션의 자동참여 여부가 N 일 때, 모달 내용 변경 / 버튼 보이기 / 모달 보이기
                $("#response-modal .modal-body h3").text("참여 승인이 필요해요!");
                $("#response-modal .modal-body p").text("미션 작성자의 승인 후 미션에 참여할 수 있어요.");
                $("#join-mission-btn-div").css("display", "block")

                $("#response-modal").show();
            }
        }

    });

    // 4-2. 미션 미참여자(회원) - [미션 참여하기] - [참여] 클릭 시, 미션 참여자 추가 로직
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

    // 5-1. 미션 참여자(회원) - [미션 탈퇴하기] 클릭 시, 모달 내용 변경 / 버튼 보이기 / 모달 보이기
    $("#btn-secession-mission-modal").on("click", function(){
        $("#response-modal .modal-body h3").text("정말 미션을 탈퇴하시겠어요?");
        $("#response-modal .modal-body p").text("기존에 제출한 미션들은 복구할 수 없습니다.");
        $("#secession-mission-btn-div").css("display", "block")

        $("#response-modal").show();

    });

    // 5-2. 미션 참여자(회원) - [미션 탈퇴하기] - [탈퇴] 클릭 시, 미션 참여자 삭제 로직
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

    /************************************************************************************************/

    /* 미션 현황 관련 함수 */

    // 미션 상세 조회 시, 현재 날짜와 비교하여 주차 및 주차별 미션현황 자동 선택
    // 현재 날짜
    var now = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
    // 주차 및 주차별 미션현황 자동 선택
    $("input[name='week-date']").each(function(idx, item){
        // input[name='week-date']의 값(날짜)와 현재 날짜가 같을 때
        if($(this).val() == now){
            // 미션현황주차
            var missionStateWeek = $(this).parents(".tab-pane").find("input[name='missionStateWeek']").val();
            // 미션 주차의 미션현황 자동 선택
            $(this).parents(".tab-pane").addClass("active")
            // 미션 주차 자동 선택
            $(this).parents(".tab-pane").parent().siblings(".nav").find("a").each(function(){
                if($(this).attr("href") == "#week" + missionStateWeek){
                    $(this).addClass("active");
                }
            });
        }
    });


    // [미션 제출] 클릭 시, Modal 띄우기
    $("#btn-mission-submit-modal").on("click", function(){
        $("#mission-submit-modal").show();
    });

    // 미션 제출 modal -  div 클릭 시
    $(".submit-mission-image-div").on("click", function(){
        $("#file").click();
    });

    // 미션 제출 modal -  미션 제출 이미지 변경 시 미리보기
    $("#file").on("change", function(){
        var changeImg = $(this)[0].files[0];

        var reader = new FileReader();
        reader.onload = function (e) {
           if(changeImg.type.match("image/*")){
             $('#submit-mission-image').attr("src", e.target.result);
             $('#submit-mission-image').css("display", "block");
             $(".submit-mission-image-desc").css("display", "none");
           }else {
             alert("이미지 형식의 파일만 가능합니다.");
           }

        }
        reader.readAsDataURL(changeImg);
    });

    // [미션 제출] 숨기기
    $(".tab-pane .table tbody tr input[name='participantsId']").each(function(){
        if($(this).val() == $("#userId").val()){
            $(this).parents("tr").find("input[name='week-date']").each(function(){
                var now = new Date(+new Date() + 3240 * 10000).toISOString().split("T")[0];
                if($(this).val() == now){
                    if($(this).siblings("input[name='missionStateSeq']").length != 0){
                        $(".mission-submit-btn-div").css("display", "none");
                    }
                }
            });
        }
    });


    // [미션 제출] - [제출]
    $("#btn-create-submit-mission").on("click", function(){
        if(validatingSubmitMission()){

            var missionStateWeek = "";
            $("input[name='week-date']").each(function(idx, item){
                if($(this).val() == now){
                    // 미션현황주차
                    missionStateWeek = $(this).parents(".tab-pane").find("input[name='missionStateWeek']").val();
                }
            });

            var missionSeq = $("#missionSeq").val();
            var userSeq = $("#userSeq").val();
            var userId = $("#userId").val();
            var submittedMissionNm = $("#submittedMissionNm").val();
            var submittedMissionDesc = $("#submittedMissionDesc").val();
            var submittedMissionImage = $("#file")[0].files[0];
            var submittedMissionDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
            var approvalYn = "N";
            var approvalDt = null;

            // 미션 생성자와 로그인 회원이 같을 때
            if($("#missionCreatedUserId").val() == $("#userId").val()){
                // 승인여부 : Y , 승인일자 : 현재시간
                approvalYn = "Y";
                approvalDt = submittedMissionDt;
            }

            var data = {
                "missionStateWeek" : missionStateWeek
                , "missionParticipants" : {
                        "missionSeq" : missionSeq
                        , "userSeq" : userSeq
                        , "userId" : userId
                }
                , "submittedMissionNm" : submittedMissionNm
                , "submittedMissionDesc" : submittedMissionDesc
                , "submittedMissionDt" : submittedMissionDt
                , "approvalYn" : approvalYn
                , "approvalDt" : approvalDt
            };

            var formData = new FormData();
            formData.append("file", submittedMissionImage);
            formData.append("requestMissionStateDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

            $.ajax({
                url : "/mission/" + missionSeq + "/submitMission"
                , type : "post"
                , data : formData
                , processData: false
                , contentType: false
                , enctype: "multipart/form-data"
                , success : function(result){
                    if(result.status = "201"){
                        location.reload();
                    }
                }
                , error : function(xhr){
                    console.log(xhr);
                }
            });
        }

    });

    // 미션 제출 modal - [닫기]
    $("#btn-cancel-submit-mission").on("click", function(){
        $("#mission-submit-modal #submittedMissionNm").val(""); // 제목 초기화
        $("#mission-submit-modal #submittedMissionDesc").val(""); // 설명 초기화
        $("#mission-submit-modal #submit-mission-image").attr("src", "").css("display", "none"); // 이미지 경로 초기화 및 안보이기
        $(".submit-mission-image-desc").css("display", "block"); // 이미지 삽입 문구 보이기
        $("#file").val(""); // 파일 비우기
        $("#mission-submit-modal").hide();
    });



    /************************************************************************************************/

    /* 승인 대기 미션 관련 js */
    // 미션 작성자와 회원의 아이디가 같을 때, 승인 대기 미션 보이기
    if($("#missionCreatedUserId").val() == $("#userId").val()){
        $(".approve-wait-submit-mission-wrapper").css("display", "block");
    }

    /************************************************************************************************/

    /* 나의 제출 미션 관련 js */
    // 나의 제출 미션 클릭 시, 해당 미션 정보 modal 띄우기
    $(".my-submit-mission-div .card").on("click", function(){
        var missionStateSeq = $(this).find("input[name='myMissionStateSeq']").val();
        var missionStateWeek = $(this).find("input[name='myMissionStateWeek']").val();

        $.ajax({
            url : "/missionState/" + missionStateWeek + "/" + missionStateSeq
            , type : "get"
            , dataType : "json"
            , success : function(result){
                if(result.status == 200){
                    console.log(result);
                    $("#my-submit-mission-modal #my-submittedMissionSeq").val(missionStateSeq);
                    $("#my-submit-mission-modal #my-submittedMissionWeek").val(missionStateWeek);
                    $("#my-submit-mission-modal #my-submittedMissionNm").val(result.data.submittedMissionNm);
                    $("#my-submit-mission-modal #my-submittedMissionDesc").val(result.data.submittedMissionDesc);
                    $("#my-submit-mission-modal #my-submit-mission-image").attr("src", result.data.submittedMissionImage);

                    if(result.data.approvalYn == "Y"){
                        $("#my-submit-mission-modal #btn-modify-my-submit-mission").css("display", "none");
                    }

                    $("#my-submit-mission-modal").show();
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    // 나의 제출 미션 modal - div 클릭 시
    $(".my-submit-mission-image-div").on("click", function(){
        $("#my-file").click();
    });

    // 나의 제출 미션 modal -  미션 제출 이미지 변경 시 미리보기
    $("#my-file").on("change", function(){
        var changeImg = $(this)[0].files[0];

        var reader = new FileReader();
        reader.onload = function (e) {
           if(changeImg.type.match("image/*")){
             $('#my-submit-mission-image').attr("src", e.target.result);
           }else {
             alert("이미지 형식의 파일만 가능합니다.");
           }
        }
        reader.readAsDataURL(changeImg);
    });

    // 나의 제출 미션 - [닫기]
    $("#btn-cancel-my-submit-mission").on("click", function(){
        $("#my-submit-mission-modal").hide();
    });

    // 나의 제출 미션 - [수정]
    $("#btn-modify-my-submit-mission").on("click", function(){
        if(validatingModifyMySubmitMission()){

            var missionStateSeq = $(this).parents("#my-submit-mission-modal").find("#my-submittedMissionSeq").val();
            var missionStateWeek = $(this).parents("#my-submit-mission-modal").find("#my-submittedMissionWeek").val();
            var submittedMissionNm = $(this).parents("#my-submit-mission-modal").find("#my-submittedMissionNm").val();
            var submittedMissionDesc = $(this).parents("#my-submit-mission-modal").find("#my-submittedMissionDesc").val();

            var data = {
               "missionStateSeq" : missionStateSeq
                , "missionStateWeek" : missionStateWeek
                , "submittedMissionNm" : submittedMissionNm
                , "submittedMissionDesc" : submittedMissionDesc
            };

            var formData = new FormData();
            formData.append("file", $("#my-file")[0].files[0]);
            formData.append("requestMissionStateDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

            $.ajax({
                url : "/missionState/" + missionStateWeek + "/" + missionStateSeq
                , type : "patch"
                , data : formData
                , processData: false
                , contentType: false
                , enctype: "multipart/form-data"
                , success : function(result){
                    if(result.status = "201"){
                        location.reload();
                    }
                }
                , error : function(xhr){
                    console.log(xhr);
                }
            });
        }

    });



    /************************************************************************************************/

}); // end $(function(){});

/************************************************************************************************/

// [미션 제출] - [제출] - 유효성 검사
function validatingSubmitMission(){
  var submittedMissionNm = $("#submittedMissionNm").val();
  var submittedMissionDesc = $("#submittedMissionDesc").val();
  var submittedMissionImage = $("#file")[0].files[0];

    if(submittedMissionNm == "" || submittedMissionNm == null){
        alert("제목을 입력해주세요.");
        return false;
    }

    if(submittedMissionDesc == "" || submittedMissionDesc == null){
        alert("미션에 대한 간단한 설명을 적어주세요.");
        return false;
    }

    if(submittedMissionImage == "" || submittedMissionImage == null){
        alert("미션을 완료한 이미지를 올려주세요.");
        return false;
    }

    return true;
}

// 나의 제출 미션 - [수정] - 유효성 검사
function validatingModifyMySubmitMission(){
  var submittedMissionNm = $("#my-submittedMissionNm").val();
  var submittedMissionDesc = $("#my-submittedMissionDesc").val();

    if(submittedMissionNm == "" || submittedMissionNm == null){
        alert("제목을 입력해주세요.");
        return false;
    }

    if(submittedMissionDesc == "" || submittedMissionDesc == null){
        alert("미션에 대한 간단한 설명을 적어주세요.");
        return false;
    }

    return true;
}


/************************************************************************************************/
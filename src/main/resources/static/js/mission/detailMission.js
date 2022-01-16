$(function(){
    /* 미션 상세 조회 */
    // 미션 시작일, 미션 종료일 날짜 포맷팅
    var formatMissionStDt = $(".mission-start-date").text().replace("T", " ");
    var formatMissionEndDt = $(".mission-end-date").text().replace("T", " ");
    $(".mission-start-date").text(formatMissionStDt);
    $(".mission-end-date").text(formatMissionEndDt);

    $("#btn-join-mission").on("click", function(){
        alert("미션 참여하기");

        var missionSeq = $("#missionSeq").val();
        var userSeq = $("#userSeq").val();
        var userId = $("#userId").val();
        var missionJoinDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
        var missionJoinYn = "Y";
        var missionJoinApprovalDt = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);

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

            }
            , error : function(xhr){

            }
        });

    });

});
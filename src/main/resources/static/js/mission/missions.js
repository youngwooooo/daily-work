$(function(){
    /* 전체 MISSION */
    // 전체 MISSION - [미션 만들기] 클릭 시
    $("#btn-create-mission").on("click", function(){
       location.href = "/mission";
    });

    // 미션 클릭 시
    $(".mission-div").on("click", function(){
        var missionSeq = $(this).find("input[name='missionSeq']").val();
        location.href = "/mission/" + missionSeq;
    });

});
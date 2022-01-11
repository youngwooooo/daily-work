$(function(){
    /* 미션 상세 조회 */
    // 미션 시작일, 미션 종료일 날짜 포맷팅
    var formatMissionStDt = $(".mission-start-date").text().replace("T", " ");
    var formatMissionEndDt = $(".mission-end-date").text().replace("T", " ");
    $(".mission-start-date").text(formatMissionStDt);
    $(".mission-end-date").text(formatMissionEndDt);

});
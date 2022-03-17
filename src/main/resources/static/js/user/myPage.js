$(function(){
    /* 회원 */
    // 회원 - [계정관리] 클릭 시
    $("#btn-move-mypage-form").on("click", function(){
        location.href = "/user/my-account";
    });

    $(".mission-info-card-div .card").on("click", function(){
        var missionSeq = $(this).find("input[name='missionSeq']").val();
        var temporaryYn = $(this).find("input[name='temporaryYn']").val();

        if(temporaryYn == "Y"){
            location.href="/mission/" + missionSeq + "/modify";
        }else {
            location.href="/mission/" + missionSeq;
        }
    });

    $(".table .board-tr").on("click", function(){
        var boardSeq = $(this).find("input[name=boardSeq]").val();
        location.href = "/board/" + boardSeq;
    });
});
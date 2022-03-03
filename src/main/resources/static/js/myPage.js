$(function(){
    /* 회원 */
    // 회원 - [계정관리] 클릭 시
    $("#btn-move-mypage-form").on("click", function(){
        location.href = "/user/my-account";
    });

    $(".card").on("click", function(){
        var missionSeq = $(this).find("input[name='missionSeq']").val();
        location.href="/mission/" + missionSeq;
    });

    $(".table .board-tr").on("click", function(){
        var boardSeq = $(this).find("input[name=boardSeq]").val();
        location.href = "/board/" + boardSeq;
    });
});
$(function(){

    // [글 쓰기]
    $("#btn-move-create-board-form").on("click", function(){
        location.href = "/board";
    });


    // 게시글 제목 클릭 시, 게시글 상세페이지 이동
    $(".board-list-div .table tbody td:nth-child(3)").on("click", function(){
        var boardSeq = $(this).siblings(".board-num").text();

        location.href = "/board/" + boardSeq;
    });
});
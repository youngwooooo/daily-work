$(function(){
    var userNo = $("#no").val();

    $("#btn-move-mypage-form").on("click", function(){
        location.href = "/user/mypage/" + userNo;
    });

});
$(function(){
    /* header */
    // [로그인] 클릭 시
    $("#btn-move-login-form").on("click", function(){
        location.href = "/login";
    });

    // [회원가입] 클릭 시
    $("#btn-move-join-form").on("click", function(){
        location.href = "/join";
    });

    // [미션] 클릭 시
    $("#btn-move-all-mission").on("click", function(){
        location.href = "/missions"
    });

    // [커뮤니티] 클릭 시
    $("#btn-move-boards").on("click", function(){
        location.href = "/boards";
    });

});
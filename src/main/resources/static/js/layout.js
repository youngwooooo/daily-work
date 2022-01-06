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

    // [로그아웃] 클릭 시
    $("#btn-move-logout").on("click", function(){
        location.href = "/logout";
    });

    // 회원 - [전체 MISSION] 클릭 시
    $("#btn-move-all-mission").on("click", function(){
        location.href = "/missions"
    });

});
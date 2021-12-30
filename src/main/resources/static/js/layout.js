$(function(){
    // header 메뉴
    $("#btn-move-login-form").on("click", function(){
        location.href = "/login";
    });

    $("#btn-move-join-form").on("click", function(){
        location.href = "/join";
    });

    $("#btn-move-logout").on("click", function(){
        location.href = "/logout";
    });

});
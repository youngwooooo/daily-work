$(function(){

    $("#btn-modify-password-save").on("click", function(){

    });

    $("#btn-cancel").on("click", function(){
        location.href = "/user/mypage";
    });

    // 비밀번호 규칙 보여주기/숨기기
    $("#password").focus(function(){
        $(".password-regExp-description").css("display", "block");
    });
    $("#password").blur(function(){
        $(".password-regExp-description").css("display", "none");
    });

});
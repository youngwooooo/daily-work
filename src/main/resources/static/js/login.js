$(function(){
    $("#login-social-google").on("click", function(){
        location.href="/oauth2/authorization/google";
    });

    $("#login-social-kakao").on("click", function(){
        location.href="/oauth2/authorization/kakao";
    });

    $("#login-social-naver").on("click", function(){
        location.href="/oauth2/authorization/naver";
    });
});
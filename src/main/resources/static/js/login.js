$(function(){

     // 엔터키로 [로그인] 버튼 누르기
     $(".login-div #userId, .login-div #userPw").on("keyup",function(key){
        if(key.keyCode==13) {
            $("#btn-login").click();
        }
     });

    // 일반 로그인 처리
    $("#btn-login").on("click", function(){
        var loginData = {
            "userId" : $("#userId").val()
            , "userPw" : $("#userPw").val()
        };

        $.ajax({
            url: "/login"
            , type: "post"
            , data: loginData
            , contentType: "application/x-www-form-urlencoded; charset=UTF-8"
            , dataType : "json"
            , success: function(result){
                if(result.result == "SUCCESS"){
                    location.href = result.uri
                }else if(result.result == "FAIL"){
                    alert(result.message);
                }
            }
           , error: function(xhr){
                console.log(xhr);
           }
        }); // end ajax

    }); // end function

    // 구글 로그인 처리
    $("#login-social-google").on("click", function(){
        location.href="/oauth2/authorization/google";
    });

    // 카카오 로그인 처리
    $("#login-social-kakao").on("click", function(){
        location.href="/oauth2/authorization/kakao";
    });

    // 네이버 로그인 처리
    $("#login-social-naver").on("click", function(){
        location.href="/oauth2/authorization/naver";
    });
});
$(function(){
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
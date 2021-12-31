$(function(){
    // 프로필 사진 클릭 시
    $("#profile-image").on("click", function(){
        $("#fileUpload").click();
    });

    // [적용] 클릭 시
    $("#btn-user-info-modify-save").on("click", function(){

    });

    // [취소] 클릭 시
    $("#btn-cancel").on("click", function(){
        location.href = "/";
    });

    // 모달 = [확인] 클릭 시
    $("#btn-move-modify-password-form").on("click", function(){
        var data = {
            "id" : $("#id").val()
            , "password" : $("#password").val()
        };

        $.ajax({
            url : "/user/mypage/valid/password"
            , type : "post"
            , data : JSON.stringify(data)
            , contentType : "application/json; charset=UTF-8"
            , dataType: "json"
            , success : function(result){
                if(result.status == 200){
                    location.href = "/user/mypage/password";
                }
            }
            , error : function(xhr){
                var error = xhr.responseJSON;
                $(".errorMsg").text(error.message);

            }
        });

    });
});
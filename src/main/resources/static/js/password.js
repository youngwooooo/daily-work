$(function(){
    // [적용] 클릭 시
    $("#btn-modify-password-save").on("click", function(){

        if(passwordSaveValidate()){
            var data = {
                "userId" : $("#userId").val()
                , "userPw" : $("#userPw").val()
            };

            $.ajax({
                url : "/user/mypage/password"
                , type : "patch"
                , data : JSON.stringify(data)
                , contentType : "application/json; charset=UTF-8"
                , dataType : "json"
                , success : function(result){
                    if(result.status == 200){
                        location.href = "/user/mypage/password/success";
                    }
                }
                , error : function(xhr){
                    var errorMsg = xhr.responseJSON.message;
                    $(".errorMsg").text(errorMsg);
                }
            });
        }

    });

    // [취소] 클릭 시
    $("#btn-cancel").on("click", function(){
        location.href = "/user/mypage";
    });

    // 비밀번호 규칙 보여주기/숨기기
    $("#userPw").focus(function(){
        $(".password-regExp-description").css("display", "block");
    });
    $("#userPw").blur(function(){
        $(".password-regExp-description").css("display", "none");
    });

});

// 비밀번호 validate 체크
function passwordSaveValidate(){
    var userPw = $("#userPw").val();
    var checkUserPw = $("#check-userPw").val();

    // 비밀번호 유효성 검사
    if(!checkRexExp("userPw", userPw)){
        return false;
    }
    // 비밀번호, 비밀번호 확인 일치 여부 확인
    if(userPw != checkUserPw){
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    return true;
}

// 비밀번호 유효성 검사
function checkRexExp(str, value){
    var rexExp = /^(?=.*[0-9])(?=.*[a-z])(?=.*\W)(?=\S+$).{10,32}$/;

    if(str == "userPw"){
        if(value == "" || value == null || !rexExp.test(value)){
            alert("비밀번호 형식에 맞게 입력해주세요.");
            return false;
        }
    }

    return true;
}


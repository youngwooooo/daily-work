$(function(){
    // 회원가입 버튼 클릭 시
    $("#btn-join-save").on("click", function(){
        if(joinSaveValidate()){
            var joinData = {
                                "id" : $("#id").val()
                                , "password" : $("#password").val()
                                , "name" : $("#name").val()
                                , "email" : $("#email").val()
                            };
            $.ajax({
                        url : "/join"
                        , type: "post"
                        , data: JSON.stringify(joinData)
                        , contentType: "application/json; charset=UTF-8"
                        , dataType : "json"
                        , success: function(result){
                            console.log(result);
                            if(result.status == 201){
                               location.href="/join/success";
                            }
                        }
                        , error: function(xhr){
                            console.log(xhr.responseJSON);
                            var errorStatus = xhr.responseJSON.status;
                            var errorMsg = xhr.responseJSON.message;
                            if(errorStatus == 409){
                                $("#login-fail-modal .join-fail-desc-div p").text(errorMsg);
                                $("#login-fail-modal").show();
                            }else if(errorStatus == 400){
                                $("#login-fail-modal").show();
                                $("#login-fail-modal .join-fail-desc-div p").text(errorMsg);
                                $("#login-fail-modal").show();
                            }
                        }
                    }); // end ajax

        } // end if

    }); // end function

    // 아이디 규칙 보여주기/숨기기
    $("#id").focus(function(){
        $(".id-regExp-description").css("display", "block");
    });
    $("#id").blur(function(){
        $(".id-regExp-description").css("display", "none");
    });
    // 비밀번호 규칙 보여주기/숨기기
    $("#password").focus(function(){
        $(".password-regExp-description").css("display", "block");
    });
    $("#password").blur(function(){
        $(".password-regExp-description").css("display", "none");
    });
    // 이름 규칙 보여주기/숨기기
    $("#name").focus(function(){
        $(".name-regExp-description").css("display", "block");
    });
    $("#name").blur(function(){
        $(".name-regExp-description").css("display", "none");
    });

    // 회원가입 실패 시 모달 창 닫기
    $("#btn-close-modal").on("click", function(){
        $("#login-fail-modal").hide();
    });

}); // end jquery

// 회원가입 validate 체크
function joinSaveValidate(){
    var id = $("#id").val();
    var password = $("#password").val();
    var CheckedPassword = $("#password-check").val();
    var name = $("#name").val();
    var email = $("#email").val();
    var phone = $("#phone").val();

    // 아이디 유효성 검사
    if(!checkRexExp("id", id)){
        return false;
    }
    // 비밀번호 유효성 검사
    if(!checkRexExp("password", password)){
        return false;
    }
    // 비밀번호, 비밀번호 확인 일치 여부 확인
    if(password != CheckedPassword){
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }
    // 이름 유효성 검사
    if(!checkRexExp("name", name)){
        return false;
    }
    // 이메일 유효성 검사
    if(!checkRexExp("email", email)){
        return false;
    }

    return true;
}

// 회원가입 데이터 유효성 검사
function checkRexExp(str, value){
    var rexExp = "";

    if(str == "id"){
        rexExp = /^(?=.*[a-z])[A-Za-z0-9]{4,20}$/;
        if(value == "" || value == null || !rexExp.test(value)){
            alert("아이디 형식에 맞게 입력해주세요.");
            return false;
        }
    }

    if(str == "password"){
        rexExp = /^(?=.*[0-9])(?=.*[a-z])(?=.*\W)(?=\S+$).{10,32}$/;
        if(value == "" || value == null || !rexExp.test(value)){
            alert("비밀번호 형식에 맞게 입력해주세요.");
            return false;
        }
    }

     if(str == "name"){
         rexExp = /^[가-힣]{2,6}$/;
         if(value == "" || value == null || !rexExp.test(value)){
             alert("이름 형식에 맞게 입력해주세요.");
             return false;
         }
     }

    if(str == "email"){
        rexExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if(value == "" || value == null || !rexExp.test(value)){
            alert("이메일 형식에 맞게 입력해주세요.");
            return false;
        }
    }

    return true;
}
$(function(){
    // ------- mypage 화면 ------- //

    // 프로필 사진 클릭 시
    $("#profile-image").on("click", function(){
        $("#file").click();
    });

    // 프로필 사진 변경 시 미리보기
    $("#file").on("change", function(){
        var changeImg = $(this)[0].files[0];

        var reader = new FileReader();
        reader.onload = function (e) {
           if(changeImg.type.match("image/*")){
             $('#profile-image').attr('src', e.target.result);
           }else {
             alert("이미지 형식의 파일만 가능합니다.");
           }

        }
        reader.readAsDataURL(changeImg);
    });

    // [적용] 클릭 시
    $("#btn-user-info-modify-save").on("click", function(){
        if(modifyUserInfoValidate()){
            var data = {
                "userId" : $("#userId").val()
                , "userNm" : $("#userNm").val()
                , "userEmail" : $("#userEmail").val()
            };

            var file = $("#file")[0].files[0];

            var formData = new FormData();
            formData.append("file", file);
            formData.append("modifyUserInfoDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));

            $.ajax({
                url : "/user/mypage"
                , type : "patch"
                , data : formData
                , processData: false
                , contentType: false
                , enctype: "multipart/form-data"
                , success : function(result){
                    console.log(result);
                    if(result.status == 200){
                        alert(result.message);
                    }
                }
                , error : function(xhr){
                    console.log(xhr);
                }
            });

        }
    });

    // [취소] 클릭 시
    $("#btn-cancel").on("click", function(){
        location.href = "/";
    });

     // 이름 규칙 보여주기/숨기기
    $("#userNm").focus(function(){
        $(".name-regExp-description").css("display", "block");
    });
    $("#userNm").blur(function(){
        $(".name-regExp-description").css("display", "none");
    });

    // ------- 비밀번호 확인 modal ------- //

    // 비밀번호 확인 modal - [확인] 클릭 시
    $("#btn-move-modify-password-form").on("click", function(){
        var data = {
            "userId" : $("#userId").val()
            , "userPw" : $("#check-userPw").val()
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

    // 모달 띄운 후 password input focus
    $("#modify-password-modal").on("shown.bs.modal", function(){
        $("#check-userPw").focus();
    });

});

// name, email validate 체크
function modifyUserInfoValidate(){
    var userNm = $("#userNm").val();
    var userEmail = $("#userEmail").val();

    // 이름 유효성 검사
    if(!checkRexExp("userNm", userNm)){
        return false;
    }
    // 이메일 유효성 검사
    if(!checkRexExp("userEmail", userEmail)){
        return false;
    }

    return true;
}

// name, email 유효성 검사
function checkRexExp(str, value){
    var rexExp = "";

     if(str == "userNm"){
         rexExp = /^[가-힣]{2,6}$/;
         if(value == "" || value == null || !rexExp.test(value)){
             alert("이름 형식에 맞게 입력해주세요.");
             return false;
         }
     }

    if(str == "userEmail"){
        rexExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if(value == "" || value == null || !rexExp.test(value)){
            alert("이메일 형식에 맞게 입력해주세요.");
            return false;
        }
    }

    return true;
}
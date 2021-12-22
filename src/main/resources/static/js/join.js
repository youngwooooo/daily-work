$(function(){
    $("#btn-id-check").on("click", function(){
        alert("중복검사 버튼 확인");
    });

    // 회원가입 버튼
    $("#btn-join-save").on("click", function(){
        alert("회원가입 버튼 호출");
        var joinData = {
                    "id" : $("#id").val()
                    , "password" : $("#password").val()
                    , "name" : $("#name").val()
                    , "email" : $("#email").val()
                    , "phone" : $("#phone").val()
                };

        $.ajax({
            url : "/join"
            , type: "post"
            , data: JSON.stringify(joinData)
            , dataType: "json"
            , contentType: "application/json; charset=UTF-8"
            , success: function(result){
                console.log(result);
            }
            , error: function(xhr){
                console.log(xhr);
            }
        }); // end ajax

    }); // end function



}); // end jquery

function joinSaveValidate(){
    alert("joinSaveValidate 호출");
    return true;
}


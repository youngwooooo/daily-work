$(function(){

    $("#response-modal").show();

    $("#btn-login-form").on("click", function(){
        location.href = "/login";
    });

    $("#btn-main-form").on("click", function(){
        location.href = "/";
    });

    $("#btn-logout").on("click", function(){
        location.href = "/logout"
    });

});
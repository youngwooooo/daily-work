$(function(){

    $("#login-success-modal").show();

    $("#btn-login-form").on("click", function(){
        location.href="/login";
    });

    $("#btn-main-form").on("click", function(){
        location.href="/";
    });

});
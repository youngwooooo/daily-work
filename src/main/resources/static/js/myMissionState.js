$(function(){
    $("#btn-move-mypage-form").on("click", function(){
        location.href = "/user/my-account";
    });

    $(".card").on("click", function(){
        var missionStateSeq = $(this).find("input[name='missionStateSeq']").val();
        var missionStateWeek = $(this).find("input[name='missionStateWeek']").val();
        var missionSeq = $(this).find("input[name='missionSeq']").val();

        $.ajax({
            url : "/missionState/" + missionStateWeek + "/" + missionStateSeq
            , type : "get"
            , dataType : "json"
            , success : function(result){
                if(result.status == 200){
                    console.log(result);
                    $("#missionState-modal #missionState-submittedMissionNm").val(result.data.submittedMissionNm);
                    $("#missionState-modal #missionState-submittedMissionDesc").val(result.data.submittedMissionDesc);
                    $("#missionState-modal #missionState-submit-mission-image").attr("src", result.data.submittedMissionImage);
                    $("#missionState-modal input[name='get-missionSeq']").val(missionSeq);

                    if(result.data.rejectionYn == "Y"){
                        $("#missionState-modal #my-rejectionDesc").val(result.data.rejectionDesc);
                        $("#missionState-modal #my-rejectionDesc").css("color", "#f55858");
                        $("#missionState-modal .my-submit-mission-rejectionDesc").before("<hr/>");
                        $("#missionState-modal .my-submit-mission-rejectionDesc").css("display", "block");
                    }

                    $("#missionState-modal").show();
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    $("#btn-move-mission").on("click", function(){
        var missionSeq = $(this).parents("#missionState-modal").find("input[name='get-missionSeq']").val();
        location.href = "/mission/" + missionSeq;
    });

    $("#btn-cancel-missionState-submit-mission").on("click", function(){
        $("#missionState-modal").hide();
    });

    $(".missionState-submit-mission-image-div").on("click", function(){
        var openImage = $(this).find("#missionState-submit-mission-image").attr("src")
        $("#open-image-modal img").attr("src", openImage);
        $("#open-image-modal").show();
    });

    $("#btn-close-open-image-modal").on("click", function(){
        $(this).parents("#open-image-modal").hide();
    });

});
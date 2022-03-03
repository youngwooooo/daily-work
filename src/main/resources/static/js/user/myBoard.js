$(function(){

    // 삭제할 boardSeq를 담을 Array
    var boardSeqArr = new Array();

    // 수정, 삭제 툴팁
    $('[data-toggle="tooltip"]').tooltip()

    // 게시글 제목 클릭 시, 해당 게시글 상세 페이지로 이동
    $(".table .board-name").on("click", function(){
        var boardSeq = $(this).siblings(".board-num").text();
        var temporary = $(this).siblings("input[name='temporary']").val();

        if(temporary == "N"){
            location.href = "/board/" + boardSeq;
        }else {
            location.href = "/board/" + boardSeq + "/modify";
        }

    });

    // 게시글 전체 선택, 전체 헤제
    $("#board-all-check").on("click", function(){
        if($(this).is(":checked")){
            $("input[name='board-check']").prop("checked", true);
        }else {
            $("input[name='board-check']").prop("checked", false);
        }
    });

    // 게시글 개별 선택 - 선택 개수에 따라 전체 선택, 전체 해제
    $("input[name='board-check']").click(function() {
        var total = $("input[name='board-check']").length;
        var checked = $("input[name='board-check']:checked").length;

        if(total != checked){
            $("#board-all-check").prop("checked", false);
        }else {
            $("#board-all-check").prop("checked", true);
        }
    });

    // [수정] - 해당 게시글 수정 페이지로 이동
    $("#btn-board-edit-form").on("click", function(){
        var boardSeqInput = $("input[name='board-check']:checked").parent().siblings("input[name='boardSeq']");

        if(boardSeqInput.length == 0){
            alert("수정할 게시글을 선택하세요.");
            return;
        }

        if(boardSeqInput.length > 1){
            alert("게시글은 1건씩 수정할 수 있습니다.");
            return;
        }

        if(boardSeqInput.length == 1){
            var boardSeq = boardSeqInput.val();
            location.href = "/board/" + boardSeqInput.val() + "/modify";
        }


    });

    // [삭제] - 삭제 경고 modal
    $("#btn-board-delete-modal").on("click", function(){
        var boardSeqInput = $("input[name='board-check']:checked").parent().siblings("input[name='boardSeq']");

        if(boardSeqInput.length > 0){
            boardSeqInput.each(function(idx, item){
                var boardSeq = item.value;
                boardSeqArr.push(boardSeq);
            });
        }else {
            alert("삭제할 게시글을 선택하세요.");
            return;
        }

        if(boardSeqArr.length > 0){
            $("#response-modal .delete-board-count").text(boardSeqArr.length);
            $("#response-modal").show();
        }

    });

    // [삭제] - 삭제 경고 modal - [삭제]
    $("#btn-board-delete").on("click", function(){
        $.ajax({
            url : "/board"
            , type : "delete"
            , data : JSON.stringify(boardSeqArr)
            , contentType: "application/json; charset=UTF-8"
            , dataType: "json"
            , success : function(result){
                console.log(result);
                if(result.status == 200){
                    location.href = "/user/mypage/my-board";
                }else {
                    alert(result.message);
                }
            }
            , error : function(xhr){
                console.log(xhr);
            }
        });
    });

    // [삭제] - 삭제 경고 modal - [취소]
    $("#btn-cancel-response-modal").on("click", function(){
        $("#response-modal").hide();
        location.reload();
    });


});
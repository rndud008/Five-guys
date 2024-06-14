$(function (){
    $("#btnDel").click(function (){
        confirm("삭제하시겠습니까?") && $("form[name='frmDelete']").submit();
    })

    // 현재 글의 id 값
    const id = $("input[name='id']").val().trim();

    // 현재 글의 댓글들을 불러온다
    LoadComment(id);

    // 댓글 작성 버튼 누르면 댓글 등록 하기.
    // 1. 어느글에 대한 댓글인지? --> 위에 id 변수에 담겨있다
    // 2. 어느 사용자가 작성한 댓글인지? --> logged_id 값
    // 3. 댓글 내용은 무엇인지?  --> 아래 content
    $("#btn_comment").click(function () {
        const content = $("#input_comment").val().trim();

        // 검증
        if (!content) {
            alert("댓글 입력을 하세요");
            $("#input_comment").focus();
            return;
        }

        // 전달할 parameter 준비 (POST)
        const data = {
            "post_id": id,
            "user_id": logged_id,
            "content": content,
        };

        $.ajax({
            url: "/comment/write",
            type: "POST",
            data: data,
            cache: false,
            success: function (data, status) {
                if(status == "success"){
                    if (data.status !== "OK") {
                        alert(data.status);
                        return;
                    }
                    LoadComment(id);  // 댓글목록 다시 업데이트
                    $("#input_comment").val('');
                }
            }
        });
    });
})

// 특정 글의 댓글목록 불러오기
function LoadComment(post_id){

}
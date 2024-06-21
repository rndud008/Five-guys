$(function (){
    $("#btnDel").click(function (){
        confirm("삭제하시겠습니까?") && $("form[name='frmDelete']").submit();
    })

    // 현재 글의 id 값
    const id = $("input[name='id']").val().trim();


    LoadLike(id);  // 좋아요 카운트 가져오기

    $("#btnLike").click(function (){

        // 현재 글의 id 값
        let id = $("input[name='id']").val().trim();
        // 현재 글의 유저 id 값
        let uid = $("input[name='uid']").val().trim();

        const data = {
            "travel_diary_post_id": id,
            "user_id": 1,
        }

        $.ajax({
            url: "/like/click",
            type: "POST",
            data: data,
            cache: false,
            success: function () {
                LoadLike(id);  // 좋아요 업데이트
            }
        });
    })

function LoadLike(travel_diary_post_id){
    $.ajax({
        url: "/like/count/" + travel_diary_post_id,
        type: "GET",
        cache: false,
        success: function (result) {
            $("#cntLike").text(result);
        }
    });
}

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
            "travel_diary_post_id": id,
            "user_id": 1,  // 로그인한 아이디 숫자대신 넣기
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
function LoadComment(travel_diary_post_id){
    $.ajax({
        url: "/comment/list/" + travel_diary_post_id,
        type: "GET",
        cache: false,
        success: function (data, status){
            if(status == "success"){
                // 서버쪽 에러 메세지 있는 경우
                if (data.status !== "OK") {
                    alert(data.status);
                    return;
                }
            }

            buildComment(data);  // 댓글 화면 렌더링

            // ★댓글목록을 불러오고 난뒤에 삭제에 대한 이벤트 리스너를 등록해야 한다
            addDelete();

        }
    })
}
function buildComment(result) {
    $("#cmt_cnt").text(result.count);  // 댓글 총 개수

    const out = [];
    result.data.forEach(comment => {
        let id = comment.id;
        let content = comment.content.trim();
        let regdate = comment.regdate;

        let user_id = parseInt(comment.user.id);
        let username = comment.user.username;
        let name = comment.user.name;

        // 삭제버튼 여부
        // (logged_id !== user_id) ? '' : 맴버권한 생성 후 바로아래 붙이기
        const delBtn = `
        <button data-cmtdel-id="${id}" title="삭제" style="height: 30px">삭제</button>
        `;

        const row = `
            <tr>
                <td><span><strong>${username}</strong><br><small>(${name})</small></span></td>
                <td>
                    <span>${content}</span>${delBtn}
                </td>
                <td><span><small>${regdate}</small></span></td>
            </tr>
        `;
        out.push(row);
    });

    $("#cmt_list").html(out.join('\n'));
}

// 댓글 삭제 버튼이 눌렸을때, 해당 댓글 삭제하는 이벤트를 등록
function addDelete() {

    // 현재 글의 id
    const id = $("input[name='id']").val().trim();

    $("[data-cmtdel-id]").click(function () {
        if(!confirm("댓글을 삭제하시겠습니까?")) return;

        // 삭제할 댓글의 id
        const comment_id = $(this).attr("data-cmtdel-id");

        $.ajax({
            url: "/comment/delete",
            type: "POST",
            cache: false,
            data: {"id": comment_id},
            success: function (data, status) {
                if (status == "success") {
                    if (data.status !== "OK") {
                        alert(data.status);
                        return;
                    }

                    // 삭제후에도 댓글 목록 불러와야 한다
                    LoadComment(id);
                }
            },
        });
    });
}
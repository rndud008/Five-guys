$(function (){
    $("#btnDel").click(function (){
        confirm("삭제하시겠습니까?") && $("form[name='frmDelete']").submit();
    })

    // 현재 글의 id 값
    const id = $("input[name='id']").val().trim();


    LoadLike(id);  // 좋아요 카운트 가져오기

    $("#btnLike").click(function (){

        const data = {
            "travel_diary_post_id": id,
            "user_id": logged_id,
        }

        $.ajax({
            url: "/like/click",
            type: "POST",
            data: data,
            cache: false,
            success: function (result) {
                // TODO 이미지 바꾸는 작업
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
    $.ajax({
        url: "/like/likeChk",
        type: "POST",
        data: {
            "travel_diary_post_id": travel_diary_post_id,
            "user_id": logged_id,},
        cache: false,
        success: function (data){
            if(data == 0){
                $("#btnLike").css("color", "#909090");
            }
            else {
                $("#btnLike").css("color", "red");
            }
        }
    });
}

    // 현재 글의 댓글들을 불러온다
    LoadComment(id);


    // 댓글 작성 버튼 누르면 댓글 등록 하기.
    $("#btn_comment").click(function () {
        const content = $("#input_comment").val().trim();

        // 검증
        if (!content) {
            alert("댓글을 입력하세요");
            $("#input_comment").focus();
            return;
        }

        // 전달할 parameter 준비 (POST)
        const data = {
            "travel_diary_post_id": id,
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
        let likecnt = comment.likecnt;

        let user_id = parseInt(comment.user.id);
        let username = comment.user.username;
        let name = comment.user.name;

        // 수정버튼 여부
        const upBtn = (logged_id !== user_id) ? '' : `
        <button data-cmtup-id="${id}" class="edit-comment-btn" title="수정" style="height: 30px">수정</button>
        `;
        // 삭제버튼 여부
        const delBtn = (logged_id !== user_id) ? '' : `
        <button data-cmtdel-id="${id}" class="delete-comment-btn" title="삭제" style="height: 30px">삭제</button>
        `;
        // 수정버튼 눌렀을 때 확인버튼
        const submitBtn = `
        <button data-cmtsub-id="${id}" class="confirm-comment-btn" title="확인" style="height: 30px; display: none;">확인</button>
        `
        // 수정버튼 눌렀을 때 취소버튼
        const cancelBtn = `
        <button data-cmtcan-id="${id}" class="cancel-comment-btn" title="취소" style="height: 30px; display: none;">취소</button>
        `
        // 댓글 좋아요 버튼
        const likeBtn = `
            <button data-cmtlike-id="${id}" class="like-comment-btn" title="좋아요" style="height: 30px">좋아요</button>
            <span class="like-count">${likecnt}</span>
        `;

        const row = `
            <tr>
                <td><span><strong>${username}</strong><br><small>(${name})</small></span></td>
                <td style="display: flex; justify-content: space-between">
                    <input readonly class="content${id}" value="${content}"/><div>${upBtn}${submitBtn}${cancelBtn}${delBtn}</div>
                </td>
                <td>${likeBtn}</td>
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

$(function () {
    // 댓글 좋아요 버튼 클릭 시
    $("body").on("click", ".like-comment-btn", function () {
        const id = $(this).attr("data-cmtlike-id");
        const uid = logged_id;
        const pid = $("input[name='id']").val().trim();

        $.ajax({
            url: "/like/clickC",
            type: "POST",
            cache: false,
            data: {
                "user_id": uid,
                "comment_id": id
            },
            success: function () {
                // 수정후 댓글 목록 불러오기
                LoadComment(pid);

            },
        });
    });

    // 댓글 수정 버튼 클릭 시
    $("body").on("click", ".edit-comment-btn", function () {
        const id = $(this).attr("data-cmtup-id");
        const $contentInput = $(`.content${id}`);

        // 입력란 수정 가능하도록 readonly 속성 제거
        $contentInput.focus();
        $contentInput.removeAttr("readonly");

        // 버튼 상태 변경
        $(this).hide();
        $(`.confirm-comment-btn[data-cmtsub-id="${id}"]`).show();
        $(`.cancel-comment-btn[data-cmtcan-id="${id}"]`).show();
    });

    // 댓글 확인 버튼 클릭 시
    $("body").on("click", ".confirm-comment-btn", function () {
        const id = $(this).attr("data-cmtsub-id");
        const $contentInput = $(`.content${id}`);
        const updatedContent = $contentInput.val().trim();

        // 입력란 readonly 속성 추가
        $contentInput.attr("readonly", true);

        // 버튼 상태 변경
        $(this).hide();
        $(`.cancel-comment-btn[data-cmtcan-id="${id}"]`).hide();
        $(`.edit-comment-btn[data-cmtup-id="${id}"]`).show();

        // 현재 글의 id
        const cid = $("input[name='id']").val().trim();

        $.ajax({
            url: "/comment/update",
            type: "POST",
            cache: false,
            data: {
                "id": id,
                "content": updatedContent
            },
            success: function (data, status) {
                if (status == "success") {
                    if (data.status !== "OK") {
                        alert(data.status);
                        return;
                    }

                    // 수정후 댓글 목록 불러오기
                    LoadComment(cid);
                }
            },
        });
    });

    // 댓글 취소 버튼 클릭 시
    $("body").on("click", ".cancel-comment-btn", function () {
        const id = $(this).attr("data-cmtcan-id");
        const $contentInput = $(`.content${id}`);

        // 입력란 readonly 속성 추가
        $contentInput.attr("readonly", true);

        // 버튼 상태 변경
        $(this).hide();
        $(`.confirm-comment-btn[data-cmtsub-id="${id}"]`).hide();
        $(`.edit-comment-btn[data-cmtup-id="${id}"]`).show();

        // 현재 글의 id
        const cid = $("input[name='id']").val().trim();
        LoadComment(cid);
    });
});
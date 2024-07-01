$(function (){
    let i = 0;
    $("#btnAdd").click(function (){
        $("#files").append(`
        <div>
           <input type="file" name="upfile${i}"/>
           <button type="button" onclick="$(this).parent().remove()">삭제</button>
        </div>`);
        i++;
    });


    $("[data-fileid-del]").click(function (){
        let fileId = $(this).attr("data-fileid-del");
        deleteFiles(fileId);
        $(this).parent().remove();
    })

    $("#subject").on('input', function() {
        let inputText = $("#subject").val().trim();

        // 입력된 텍스트가 최대 길이를 초과하는 경우
        if (inputText.length > 100) {
            // 초과된 부분을 잘라내고 다시 textarea에 설정
            $("#subject").val(inputText.slice(0, 100));
        }
    });
});

function deleteFiles(fileId) {
    $("#delFiles").append(`<input type='hidden' name='delfile' value='${fileId}'/>`);
}

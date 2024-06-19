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
});

function deleteFiles(fileId) {
    $("#delFiles").append(`<input type='hidden' name='delfile' value='${fileId}'/>`);
}

$(function () {
    let i = 0;
    $("#btnAdd").click(function () {
        $("#files").append(`
            <div>
               <input type="file" name="upfile${i}"/>
               <button type="button" onclick="$(this).parent().remove()">삭제</button>
            </div>`);
        i++;
    });
});


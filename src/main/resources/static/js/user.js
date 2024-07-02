$(function () {
    $('#delUser').click(function (event) {
        event.preventDefault();

        if(!confirm("계정을 삭제하시겠습니까?")) return;

        const user = user;

        $.ajax({
            url: "/user/delete",
            type: "POST",
            cache: false,
            data: { "user": user },
            success: function (response) {
                console.log("AJAX success:", response);
            },
            error: function (xhr, status, error) {
                console.error("AJAX error:", error);
            }
        });
    })
})
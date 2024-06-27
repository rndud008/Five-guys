function loadMore() {
    $.get("/api/festival/blogs", { id: postId, offset: offset }, function(data) {
        if (data.length > 0) {
            let blogList = $('#blogList');
            data.forEach(function(blog) {
                blogList.append(
                    '<li>' +
                    '<div>' +
                    '<a href="' + blog.link + '" target="_blank" class="tit">' +
                    '<strong>' + blog.title + '</strong>' +
                    '<span>' + blog.postdate.substring(2, 4) + '.' + blog.postdate.substring(4, 6) + '.' + blog.postdate.substring(6, 8) + '</span>' +
                    '</a>' +
                    '<a href="' + blog.link + '" target="_blank" class="desc">' +
                    blog.description +
                    '</a>' +
                    '</div>' +
                    '</li>'
                );
            });
            offset += 3; // 다음 요청을 위해 offset 값을 증가
        } else {
            $('#loadMore').hide(); // 더 이상 데이터가 없을 때 "더보기" 버튼 숨기기
        }
    });
}
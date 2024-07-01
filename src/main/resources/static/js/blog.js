let offset = 5; // 초기 offset 값 설정
let totalBlogs = 0; // 총 블로그 개수

$(document).ready(function () {

    // 페이지 로드 시 총 블로그 개수를 가져옴
    $.get("/api/blogs/count", {id: postContentid}, function (data) {
        totalBlogs = data;
    });
});

function loadMore() {
    console.log("클릭")
    $.get("/api/blogs", {id: postContentid, offset: offset}, function (data) {
        if (data.length > 0) {
            let blogList = $('#blogList');
            data.forEach(function (blog) {
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

            // 현재 로드된 블로그 수와 총 블로그 수를 비교
            if (offset >= totalBlogs) {
                $('#loadMore').hide(); // 모든 블로그가 로드되면 "더보기" 버튼 숨기기
            }
        } else {
            $('#loadMore').hide(); // 더 이상 데이터가 없을 때 "더보기" 버튼 숨기기
        }
    });
}
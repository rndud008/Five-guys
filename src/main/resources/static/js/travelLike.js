
$(document).ready(function () {

    $('.like-button').on('click', function () {

        if (loggedUser === null) {
            alert('로그인이 필요합니다.');
            // window.location.href = '/user/login';
            $('#modalSign').show();
        }

        $.ajax({
            url: '/like/postTravelPostLike',
            type: 'POST',
            data: {
                travelPostId: postId
            },
            success: function (response) {
                if (response === ' ') {
                    $('.like-button').html(`<i class="fa-regular fa-thumbs-up"></i>`)
                } else {
                    $('.like-button').html(`<i class="fa-solid fa-thumbs-up"></i>`)
                }

                updateLikeCount();
            },
            error: function (xhr, status, error) {
                console.error('Error fetching search results:', error);
            }
        });
    });

    loadLikeStatus();

});

function loadLikeStatus() {
    console.log("loadLikeStatus 실행")

    $.ajax({
        url: '/like/getTravelPostLike',
        type: 'GET',
        data: {
            travelPostId: postId
        },
        success: function (response) {
            if (response === ' ') {
                $('.like-button').html(`<i class="fa-regular fa-thumbs-up"></i>`)
            } else {
                $('.like-button').html(`<i class="fa-solid fa-thumbs-up"></i>`)
            }

            updateLikeCount();
        },
        error: function (xhr, status, error) {
            console.error('Error fetching search results:', error);

        }
    });
}

function updateLikeCount() {
    $.ajax({
        url: '/like/travelPostLikeCount',
        type: 'GET',
        data: {
            travelPostId: postId
        },
        success: function (response) {
            $('.like-count').html(response)
        },
        error: function (xhr, status, error) {
            console.error('Error fetching search results:', error);
        }
    });
}
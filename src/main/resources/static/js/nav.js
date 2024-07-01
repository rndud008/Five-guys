// const navbarContainer = document.querySelector('header');
// const navbarHeight = navbarContainer.offsetHeight;
//
// window.addEventListener('scroll', function () {
//     // 스크롤 위치가 navbar 높이를 넘어섰는지 확인합니다.
//     if (window.pageYOffset >= navbarHeight) {
//         navbarContainer.classList.add('sticky');
//     } else {
//         navbarContainer.classList.remove('sticky');
//     }
// });

$(document).ready(function (){

    const navbarContainer = $('header');
    const navbarHeight = navbarContainer.outerHeight();

    $(window).scroll(function() {
        // 스크롤 위치가 navbar 높이를 넘어섰는지 확인합니다.
        if ($(window).scrollTop() >= navbarHeight) {
            navbarContainer.addClass('sticky');
        } else {
            navbarContainer.removeClass('sticky');
        }
    });

    $('.loginSuccess').click(function (){
        if ($('.login-profile').is(':visible')) {
            $('.login-profile').hide(); // 보이는 상태라면 숨깁니다.
        } else {
            $('.login-profile').show(); // 숨겨진 상태라면 보이게 합니다.
        }
    })
});
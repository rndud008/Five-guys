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

    $('.login-username').click(function (){
        $('.login-profile').toggleClass('show');

    })

    // const $navmenu = $('.nav-menu');
    // const $ham = $('.fa-solid.fa-bars');
    //
    // $ham.click(function () {
    //     $navmenu.slideToggle();
    // });
    // if($ham.is(':hidden')){
    //     $navmenu.css('display','flex');
    // }
    const $navmenu = $('.nav-menu');
    const $ham = $('.fa-solid.fa-bars');

    // Toggle the nav menu when the hamburger menu is clicked
    $ham.click(function() {
        $navmenu.slideToggle();
    });

    // Function to set the display property based on window width
    function checkWidth() {
        if ($(window).width() > 1300) {
            $navmenu.css('display', 'flex');
        } else if (!$ham.is(':visible')) {
            $navmenu.css('display', 'none');
        }
    }

    // Check the width on page load
    checkWidth();

    // Check the width whenever the window is resized
    $(window).resize(function() {
        checkWidth();
    });
});

function loadRegisterPage() {
    // 현재 페이지의 fragment 부분을 교체하는 코드 작성
    // 예를 들어 Thymeleaf의 th:fragment 기능을 사용하여 register.html 파일의 특정 fragment를 로드할 수 있습니다.
    $('#content').load('/ t/register');
}
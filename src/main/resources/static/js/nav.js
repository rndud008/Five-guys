const navbarContainer = document.querySelector('header');
const navbarHeight = navbarContainer.offsetHeight;

window.addEventListener('scroll', function () {
    // 스크롤 위치가 navbar 높이를 넘어섰는지 확인합니다.
    if (window.pageYOffset >= navbarHeight) {
        navbarContainer.classList.add('sticky');
    } else {
        navbarContainer.classList.remove('sticky');
    }
});

function loadRegisterPage() {
    // 현재 페이지의 fragment 부분을 교체하는 코드 작성
    // 예를 들어 Thymeleaf의 th:fragment 기능을 사용하여 register.html 파일의 특정 fragment를 로드할 수 있습니다.
    $('#content').load('/fragment/register');
}
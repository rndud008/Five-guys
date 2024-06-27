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
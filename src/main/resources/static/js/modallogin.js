const modal = document.getElementById('modalSignin');

// HTML에서 모달창 열기 버튼 선택
const openModalBtn = document.querySelector('.login button');

// 모달창 요소가 존재하는지 확인
if (modal !== null) {
    // 모달창 열기 이벤트 리스너 등록
    openModalBtn.addEventListener('click', () => {
        modal.style.display = 'block';
        centerModal();
    });

    // 모달창 닫기 버튼 선택
    const closeModalBtn = document.querySelector('.modal .close');

    // 모달창 닫기 이벤트 리스너 등록
    closeModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // 모달창 외부 클릭 시 닫기
    window.addEventListener('click', (event) => {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });

    // 모달 창 중앙 정렬 함수
    function centerModal() {
        const dialog = modal.querySelector('.modal-dialog');

        // 모달 창의 높이가 화면 높이보다 작을 경우
        if (dialog.offsetHeight < window.innerHeight) {
            dialog.style.marginTop = (window.innerHeight - dialog.offsetHeight) / 2 + 'px';
        } else {
            dialog.style.marginTop = '';
        }
    }

    // 새로고침 시 모달창 열리지 않도록 설정
    window.addEventListener('DOMContentLoaded', () => {
        modal.style.display = 'none';
    });
}

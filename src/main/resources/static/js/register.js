$(document).ready(function() {
    $('#email_domain').change(function() {
        var selectedDomain = $(this).val();
        if (selectedDomain === '') {    // "직접입력" 이 선택된 경우
            // 입력 필드를 비우고, 읽기 전용 속성 해제, 포커스 설정
            $('#custom_domain').val('').prop('readonly', false).focus();

        } else {   // 다른 domain 이 선택된 경우
            // 입력 필드에 선택된 domain 값 삽입, 읽기 전용 속성으로 설정
            $('#custom_domain').val(selectedDomain).prop('readonly', true);
        }
    });
});

$(document).ready(function() {
    const $modal = $('#modalSign');
    const $openModalBtn = $('.login button');
    const $closeModalBtn = $('.modal .close');

    if ($modal.length) {
        $openModalBtn.on('click', function() {
            $modal.show();
            // centerModal();
        });

        $closeModalBtn.on('click', function() {
            $modal.hide();
        });

        $(window).on('click', function(event) {
            if ($(event.target).is($modal)) {
                $modal.hide();
            }
        });

        // 새로고침 시 모달창 열리지 않도록 설정
        $modal.hide();
    }
});

function centerModal() {
    const $dialog = $modal.find('.modal-dialog');

    if ($dialog.outerHeight() < $(window).height()) {
        $dialog.css('marginTop', ($(window).height() - $dialog.outerHeight()) / 2 + 'px');
    } else {
        $dialog.css('marginTop', '');
    }
}



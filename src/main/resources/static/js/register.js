function updateCustomDomain() {
    var customDomainInput = document.getElementById('custom_domain');
    var domainSelect = document.getElementById('domain');
    if (domainSelect.value === '') {
        customDomainInput.value = '';
        customDomainInput.disabled = false;
    } else {
        customDomainInput.value = domainSelect.value;
        customDomainInput.disabled = true;
    }
}

$(function () {
    $('[name="registerform"]').submit(function () {
        if ($('[name="custom_domain"]').val() == null) {
            $('[name="email"]').val(
                $('[name="email_id"]').val() + '@' + $('[name="domain"]').val()
            );
            return true;
        } else {
            $('[name="email"]').val(
                $('[name="email_id"]').val() + '@' + $('[name="custom_domain"]').val()
            );
            return true;
        }
        // $(this).submit();
    });
});
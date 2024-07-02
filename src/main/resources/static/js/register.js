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
    $('[name="registerform"]').submit( function() {
        $('[name="email"]').val(
            $('[name="email_id"]').val() + '@' + $('[name="domain"]').val()
        );
        console.log($('[name="email"]').val());
        // $(this).submit();
        return true;
    });
});
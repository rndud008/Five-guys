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
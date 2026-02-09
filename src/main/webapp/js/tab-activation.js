// Tab activation based on URL parameter
document.addEventListener('DOMContentLoaded', function () {
    console.log('[TAB] DOM Loaded - Initializing tab activation...');
    console.log('[TAB] URL:', window.location.href);

    const urlParams = new URLSearchParams(window.location.search);
    const tabParam = urlParams.get('tab');

    console.log('[TAB] Tab parameter:', tabParam);

    if (tabParam) {
        console.log('[TAB] Attempting to activate tab:', tabParam);

        const targetBtn = document.querySelector(`button[data-bs-target="#${tabParam}"]`);
        const targetPane = document.getElementById(tabParam);

        console.log('[TAB] Target button:', targetBtn);
        console.log('[TAB] Target pane:', targetPane);

        if (targetBtn && targetPane) {
            // Use Bootstrap's Tab API
            const tab = new bootstrap.Tab(targetBtn);
            tab.show();
            console.log('[TAB] Tab activated successfully!');
        } else {
            console.error('[TAB] Elements not found!');
            console.log('[TAB] All buttons with data-bs-target:', document.querySelectorAll('button[data-bs-target]'));
        }
    } else {
        console.log('[TAB] No tab parameter in URL');
    }
});

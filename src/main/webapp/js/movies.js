// Giữ trạng thái checkbox dựa trên URL params
// Giữ trạng thái checkbox dựa trên URL params
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const selectedGenres = urlParams.getAll('genres');
    
    // Đánh dấu các checkbox đã chọn
    selectedGenres.forEach(genre => {
        const checkbox = document.querySelector(`input[value="${genre}"]`);
        if (checkbox) {
            checkbox.checked = true;
        }
    });
    
    // Giữ lại keyword trong search input
    const searchInput = document.querySelector('input[name="search"]');
    const searchKeyword = urlParams.get('search');
    if (searchInput && searchKeyword) {
        searchInput.value = searchKeyword;
    }
});

// Submit form khi thay đổi genre
// Note: Form sẽ tự động giữ lại các checkbox đã chọn và search keyword

// Hover effects for genre chips
document.querySelectorAll('.genre-chip label').forEach(label => {
    label.addEventListener('mouseenter', function () {
        if (!this.previousElementSibling.checked) {
            this.style.background = 'rgba(102, 126, 234, 0.2)';
            this.style.borderColor = '#667eea';
        }
    });
    label.addEventListener('mouseleave', function () {
        if (!this.previousElementSibling.checked) {
            this.style.background = 'rgba(255,255,255,0.05)';
            this.style.borderColor = 'rgba(255,255,255,0.2)';
        }
    });
});

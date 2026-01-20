// Main JavaScript file for Music Label Platform

document.addEventListener('DOMContentLoaded', function() {


    // Form validation enhancement
    const forms = document.querySelectorAll('form[needs-validation]');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // File upload preview (for track submissions)
    const fileInputs = document.querySelectorAll('input[type="file"][data-preview]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const previewElement = document.getElementById(this.getAttribute('data-preview'));
            if (previewElement && this.files.length > 0) {
                previewElement.textContent = `Selected file: ${this.files[0].name}`;
            }
        });
    });

    // Auto-dismiss alerts
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Password strength indicator
    const passwordInputs = document.querySelectorAll('input[type="password"][data-strength]');
    passwordInputs.forEach(input => {
        input.addEventListener('input', function() {
            const strengthIndicator = document.getElementById(this.getAttribute('data-strength'));
            if (strengthIndicator) {
                const strength = calculatePasswordStrength(this.value);
                updateStrengthIndicator(strengthIndicator, strength);
            }
        });
    });
});

function calculatePasswordStrength(password) {
    let strength = 0;

    if (password.length >= 8) strength++;
    if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
    if (password.match(/\d/)) strength++;
    if (password.match(/[^a-zA-Z\d]/)) strength++;

    return strength;
}

function updateStrengthIndicator(indicator, strength) {
    const classes = ['bg-danger', 'bg-warning', 'bg-info', 'bg-success'];
    const texts = ['Very Weak', 'Weak', 'Good', 'Strong'];

    indicator.className = `password-strength ${classes[strength] || 'bg-danger'}`;
    indicator.style.width = `${(strength / 4) * 100}%`;
    indicator.setAttribute('aria-valuenow', strength);

    const textElement = indicator.parentElement.querySelector('.strength-text');
    if (textElement) {
        textElement.textContent = texts[strength] || 'Very Weak';
    }
}

// AJAX helper functions
function submitFormAjax(formElement, successCallback, errorCallback) {
    const formData = new FormData(formElement);

    fetch(formElement.action, {
        method: formElement.method,
        body: formData,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            successCallback(data);
        } else {
            errorCallback(data);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        errorCallback({ error: 'Network error occurred' });
    });
}

// Utility function to show loading state
function setLoadingState(button, isLoading) {
    if (isLoading) {
        button.disabled = true;
        button.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...';
    } else {
        button.disabled = false;
        button.innerHTML = button.getAttribute('data-original-text');
    }
}
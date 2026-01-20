<#ftl output_format="HTML" auto_esc=true>
<#assign title="Success">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="success-container">
        <div class="success-content">
            <div class="success-icon">✅</div>
            <h1>Success!</h1>

            <#if successMessage??>
                <div class="success-message">
                    <p>${successMessage}</p>
                </div>
            <#else>
                <div class="success-message">
                    <p>Your request was completed successfully.</p>
                </div>
            </#if>

            <#if additionalInfo??>
                <div class="additional-info">
                    <p>${additionalInfo}</p>
                </div>
            </#if>

            <div class="success-actions">
                <#if continueUrl??>
                    <a href="${contextPath}${continueUrl}" class="btn btn-primary">
                        <#if continueText??>${continueText}<#else>Continue</#if>
                    </a>
                </#if>

                <#if secondaryUrl??>
                    <a href="${contextPath}${secondaryUrl}" class="btn btn-secondary">
                        <#if secondaryText??>${secondaryText}<#else>Back to Dashboard</#if>
                    </a>
                <#else>
                    <a href="${contextPath}/dashboard" class="btn btn-secondary">Dashboard</a>
                </#if>

                <#if homeUrl!true>
                    <a href="${contextPath}/" class="btn btn-outline">Home Page</a>
                </#if>
            </div>

            <#if autoRedirect?? && autoRedirect.url??>
                <div class="auto-redirect">
                    <p>Redirecting in <span id="countdown">${autoRedirect.delay!5}</span> seconds...</p>
                </div>

                <script>
                    let seconds = ${autoRedirect.delay!5};
                    const countdownElement = document.getElementById('countdown');
                    const redirectUrl = '${contextPath}${autoRedirect.url}';

                    const countdown = setInterval(function() {
                        seconds--;
                        countdownElement.textContent = seconds;

                        if (seconds <= 0) {
                            clearInterval(countdown);
                            window.location.href = redirectUrl;
                        }
                    }, 1000);
                </script>
            </#if>
        </div>
    </div>
</@layout.base>

<style>
.success-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
    padding: 2rem;
}

.success-content {
    text-align: center;
    background: white;
    padding: 3rem;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    max-width: 500px;
    width: 100%;
}

.success-icon {
    font-size: 4rem;
    margin-bottom: 1rem;
}

.success-content h1 {
    color: #4CAF50;
    margin-bottom: 1.5rem;
    font-size: 2.5rem;
}

.success-message {
    background: #f8f9fa;
    padding: 1.5rem;
    border-radius: 8px;
    border-left: 4px solid #4CAF50;
    margin-bottom: 1.5rem;
}

.success-message p {
    margin: 0;
    color: #666;
    font-size: 1.1rem;
    line-height: 1.5;
}

.additional-info {
    background: #e8f5e8;
    padding: 1rem;
    border-radius: 8px;
    border: 1px solid #4CAF50;
    margin-bottom: 2rem;
}

.additional-info p {
    margin: 0;
    color: #2e7d32;
    font-size: 0.9rem;
}

.success-actions {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-bottom: 2rem;
    flex-wrap: wrap;
}

.auto-redirect {
    color: #666;
    font-size: 0.9rem;
    border-top: 1px solid #eee;
    padding-top: 1rem;
}

.auto-redirect p {
    margin: 0;
}

@media (max-width: 768px) {
    .success-content {
        padding: 2rem 1rem;
    }

    .success-actions {
        flex-direction: column;
        align-items: center;
    }

    .success-actions .btn {
        width: 200px;
    }
}
</style>
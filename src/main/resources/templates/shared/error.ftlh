<#ftl output_format="HTML" auto_esc=true>
<#assign title="Error ${statusCode!500}">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">⚠️</div>
            <h1>Oops! Something went wrong</h1>

            <#if errorMessage??>
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            <#else>
                <div class="error-message">
                    <#if statusCode??>
                        <p><strong>Error ${statusCode}</strong></p>
                    </#if>
                    <p>We encountered an unexpected error while processing your request.</p>
                </div>
            </#if>

            <div class="error-details">
                <details>
                    <summary>Technical Details</summary>
                    <div class="technical-info">
                        <p><strong>Timestamp:</strong> ${.now?string("yyyy-MM-dd HH:mm:ss")}</p>
                        <p><strong>Path:</strong> ${currentPath!"Unknown"}</p>
                        <#if exception??>
                            <p><strong>Exception:</strong> ${exception.class.simpleName}</p>
                        </#if>
                    </div>
                </details>
            </div>

            <div class="error-actions">
                <a href="javascript:history.back()" class="btn btn-secondary">Go Back</a>
                <a href="${contextPath}/" class="btn btn-primary">Home Page</a>
                <#if user??>
                    <a href="${contextPath}/dashboard" class="btn btn-outline">Dashboard</a>
                </#if>
            </div>

            <div class="error-help">
                <p>If this problem persists, please contact support.</p>
            </div>
        </div>
    </div>
</@layout.base>

<style>
.error-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
    padding: 2rem;
}

.error-content {
    text-align: center;
    background: white;
    padding: 3rem;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    max-width: 600px;
    width: 100%;
}

.error-icon {
    font-size: 4rem;
    margin-bottom: 1rem;
}

.error-content h1 {
    color: #333;
    margin-bottom: 1.5rem;
    font-size: 2rem;
}

.error-message {
    background: #f8f9fa;
    padding: 1.5rem;
    border-radius: 8px;
    border-left: 4px solid #f44336;
    margin-bottom: 2rem;
    text-align: left;
}

.error-message p {
    margin: 0.5rem 0;
    color: #666;
}

.error-details {
    margin-bottom: 2rem;
    text-align: left;
}

.error-details summary {
    cursor: pointer;
    padding: 0.5rem;
    background: #f1f3f4;
    border-radius: 5px;
    font-weight: 500;
}

.technical-info {
    padding: 1rem;
    background: #f8f9fa;
    border-radius: 5px;
    margin-top: 0.5rem;
    font-family: monospace;
    font-size: 0.9rem;
}

.technical-info p {
    margin: 0.25rem 0;
}

.error-actions {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-bottom: 2rem;
    flex-wrap: wrap;
}

.error-help {
    color: #666;
    font-size: 0.9rem;
    border-top: 1px solid #eee;
    padding-top: 1rem;
}

@media (max-width: 768px) {
    .error-content {
        padding: 2rem 1rem;
    }

    .error-actions {
        flex-direction: column;
        align-items: center;
    }

    .error-actions .btn {
        width: 200px;
    }
}
</style>
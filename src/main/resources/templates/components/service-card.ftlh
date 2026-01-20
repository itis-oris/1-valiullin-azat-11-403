<#macro service_card service showActions=true>
<div class="service-card">
    <div class="service-header">
        <h4>${service.name!}</h4>
        <#if service.basePrice?? && service.basePrice gt 0>
            <span class="price">$${service.basePrice!0}</span>
        <#else>
            <span class="price free">Free</span>
        </#if>
    </div>

    <div class="service-body">
        <#if service.description??>
            <p class="description">${service.description!}</p>
        </#if>

        <div class="service-meta">
            <#if service.submissionCount??>
                <span class="meta-item">
                    <strong>${service.submissionCount!0}</strong> submissions
                </span>
            </#if>
            <#if service.avgRating??>
                <span class="meta-item">
                    <strong>${service.avgRating?string("0.0")!0}</strong> ★
                </span>
            </#if>
        </div>
    </div>

    <div class="service-actions">
        <#if showActions>
            <#if user?? && user.role == 'ARTIST'>
                <a href="${contextPath!}/submission/create?serviceId=${service.id!}"
                   class="btn btn-sm btn-primary">
                    Select Service
                </a>
            <#elseif user?? && user.role == 'LABEL'>
                <a href="${contextPath!}/service/edit/${service.id!}"
                   class="btn btn-sm btn-outline">
                    Edit
                </a>
                <form action="${contextPath!}/service/delete/${service.id!}" method="post"
                      class="inline-form" onsubmit="return confirm('Delete this service?')">
                    <input type="hidden" name="csrfToken" value="${csrfToken!}">
                    <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                </form>
            </#if>
        </#if>
    </div>
</div>

<style>
.service-card {
    background: white;
    border-radius: 12px;
    padding: 1.5rem;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    border: 1px solid #e1e5e9;
    transition: all 0.3s ease;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.service-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 5px 20px rgba(0,0,0,0.15);
}

.service-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 1rem;
}

.service-header h4 {
    margin: 0;
    color: #333;
    font-size: 1.2rem;
    flex: 1;
    margin-right: 1rem;
}

.price {
    background: #667eea;
    color: white;
    padding: 0.3rem 0.8rem;
    border-radius: 20px;
    font-weight: bold;
    font-size: 0.9rem;
    white-space: nowrap;
}

.price.free {
    background: #4CAF50;
}

.service-body {
    flex: 1;
    margin-bottom: 1.5rem;
}

.description {
    color: #666;
    line-height: 1.5;
    margin-bottom: 1rem;
    font-size: 0.95rem;
}

.service-meta {
    display: flex;
    gap: 1rem;
    flex-wrap: wrap;
}

.meta-item {
    font-size: 0.85rem;
    color: #888;
    display: flex;
    align-items: center;
    gap: 0.3rem;
}

.meta-item strong {
    color: #333;
    font-weight: 600;
}

.service-actions {
    display: flex;
    gap: 0.5rem;
    align-items: center;
    flex-wrap: wrap;
}

.btn-sm {
    padding: 0.4rem 0.8rem;
    font-size: 0.85rem;
}

.btn-outline {
    background: transparent;
    color: #667eea;
    border: 1px solid #667eea;
}

.btn-outline:hover {
    background: #667eea;
    color: white;
}

.btn-danger {
    background: #f44336;
    color: white;
    border: none;
}

.btn-danger:hover {
    background: #d32f2f;
}

.inline-form {
    display: inline;
    margin: 0;
}
</style>
</#macro>
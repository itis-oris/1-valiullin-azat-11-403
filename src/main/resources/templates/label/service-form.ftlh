<#ftl output_format="HTML" auto_esc=true>
<#assign title=(isEdit)?then('Edit Service', 'Create Service')>
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="service-form-container">
        <div class="form-header">
            <h1>${(isEdit)?then('Edit Service', 'Create New Service')}</h1>
            <p>Define a service that artists can request from your label</p>
        </div>

        <div class="form-content">
            <form action="${contextPath}/service/${(isEdit)?then('edit/' + service.id?c, 'create')}" method="post">
                <#if isEdit>
                    <input type="hidden" name="serviceId" value="${service.id}">
                </#if>

                <div class="form-group">
                    <label for="name">Service Name *</label>
                    <input type="text" id="name" name="name" required
                           value="${(service.name)!}" maxlength="100"
                           placeholder="e.g., Track Release, Music Production, Marketing Package">
                </div>

                <div class="form-group">
                    <label for="description">Service Description *</label>
                    <textarea id="description" name="description" required rows="4"
                              placeholder="Describe what artists will receive with this service, including deliverables, timeline, and any requirements...">${(service.description)!}</textarea>
                    <small>Be clear about what artists can expect</small>
                </div>

                <div class="form-group">
                    <label for="basePrice">Price ($ USD)</label>
                    <input type="number" id="basePrice" name="basePrice"
                           value="${(service.basePrice)!0}" min="0" max="10000" step="0.01"
                           placeholder="0.00 for free services">
                    <small>Set to 0 for free services (like track submission review)</small>
                </div>

                <div class="form-group">
                    <label>Service Type</label>
                    <div class="service-type-options">
                        <label class="radio-option">
                            <input type="radio" name="serviceType" value="SUBMISSION"
                                   <#if !isEdit || ((service.basePrice)!0) == 0>checked</#if>>
                            <div class="option-content">
                                <h4>🎵 Track Submission</h4>
                                <p>Free service for artists to submit tracks for review</p>
                            </div>
                        </label>
                        <label class="radio-option">
                            <input type="radio" name="serviceType" value="PREMIUM"
                                   <#if isEdit && ((service.basePrice)!0) gt 0>checked</#if>>
                            <div class="option-content">
                                <h4>💰 Premium Service</h4>
                                <p>Paid service with specific deliverables</p>
                            </div>
                        </label>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        ${(isEdit)?then('Update Service', 'Create Service')}
                    </button>
                    <a href="${contextPath}/service/manage" class="btn btn-secondary">Cancel</a>
                    <#if isEdit>
                        <button type="button" class="btn btn-danger"
                                onclick="confirmDelete(${service.id})">
                            Delete Service
                        </button>
                    </#if>
                </div>
            </form>
        </div>

        <div class="form-guidance">
            <h3>Service Creation Guidelines</h3>
            <ul>
                <li><strong>Track Release Service:</strong> Should always be free and available for artists to submit music</li>
                <li><strong>Clear Pricing:</strong> Be transparent about costs and what's included</li>
                <li><strong>Realistic Timelines:</strong> Set achievable delivery timelines</li>
                <li><strong>Requirements:</strong> Clearly state any prerequisites or requirements</li>
            </ul>
        </div>
    </div>

    <#if isEdit>
    <script>
    function confirmDelete(serviceId) {
        if (confirm('Are you sure you want to delete this service? Existing submissions will be preserved but no new submissions can be made.')) {
            fetch('${contextPath}/service/delete/' + serviceId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            }).then(response => {
                if (response.ok) {
                    window.location.href = '${contextPath}/service/manage?success=1';
                } else {
                    alert('Error deleting service');
                }
            });
        }
    }

    // Auto-set price based on service type
    document.querySelectorAll('input[name="serviceType"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const priceInput = document.getElementById('basePrice');
            if (this.value === 'SUBMISSION') {
                priceInput.value = '0';
            }
        });
    });
    </script>
    </#if>

    <style>
    .service-form-container {
        max-width: 800px;
        margin: 0 auto;
        padding: 2rem;
    }

    .form-header {
        text-align: center;
        margin-bottom: 2rem;
        color: white;
    }

    .form-header h1 {
        margin-bottom: 0.5rem;
        font-size: 2.5rem;
    }

    .form-content {
        background: rgba(255, 255, 255, 0.1);
        backdrop-filter: blur(10px);
        padding: 2rem;
        border-radius: 15px;
        border: 1px solid rgba(255, 255, 255, 0.2);
        margin-bottom: 2rem;
    }

    .form-group {
        margin-bottom: 1.5rem;
    }

    .form-group label {
        display: block;
        margin-bottom: 0.5rem;
        color: white;
        font-weight: bold;
    }

    .form-group input,
    .form-group textarea {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid rgba(255, 255, 255, 0.2);
        border-radius: 8px;
        background: rgba(255, 255, 255, 0.1);
        color: white;
        font-size: 1rem;
        font-family: inherit;
    }

    .form-group input:focus,
    .form-group textarea:focus {
        outline: none;
        border-color: #667eea;
    }

    .form-group textarea {
        resize: vertical;
        min-height: 100px;
    }

    .form-group small {
        display: block;
        margin-top: 0.25rem;
        color: rgba(255, 255, 255, 0.7);
        font-size: 0.875rem;
    }

    .service-type-options {
        display: flex;
        gap: 1rem;
        margin-top: 0.5rem;
    }

    .radio-option {
        flex: 1;
        border: 2px solid rgba(255, 255, 255, 0.2);
        border-radius: 10px;
        padding: 1.5rem;
        cursor: pointer;
        transition: all 0.3s ease;
        background: rgba(255, 255, 255, 0.05);
    }

    .radio-option:hover {
        border-color: #667eea;
        background: rgba(102, 126, 234, 0.1);
    }

    .radio-option input[type="radio"] {
        display: none;
    }

    .radio-option input[type="radio"]:checked + .option-content {
        color: #ffd700;
    }

    .radio-option input[type="radio"]:checked {
        background: rgba(255, 215, 0, 0.1);
        border-color: #ffd700;
    }

    .option-content h4 {
        margin: 0 0 0.5rem 0;
        font-size: 1.1rem;
        color: inherit;
    }

    .option-content p {
        margin: 0;
        color: rgba(255, 255, 255, 0.8);
        font-size: 0.9rem;
    }

    .form-actions {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        margin-top: 2rem;
        flex-wrap: wrap;
    }

    .btn {
        padding: 12px 24px;
        border: none;
        border-radius: 8px;
        font-weight: bold;
        text-decoration: none;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 1rem;
        text-align: center;
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
    }

    .btn-primary {
        background: #667eea;
        color: white;
    }

    .btn-primary:hover {
        background: #5a6fd8;
        transform: translateY(-2px);
    }

    .btn-secondary {
        background: rgba(255, 255, 255, 0.2);
        color: white;
    }

    .btn-secondary:hover {
        background: rgba(255, 255, 255, 0.3);
        transform: translateY(-2px);
    }

    .btn-danger {
        background: rgba(239, 68, 68, 0.8);
        color: white;
    }

    .btn-danger:hover {
        background: rgba(220, 38, 38, 0.9);
        transform: translateY(-2px);
    }

    .form-guidance {
        background: rgba(255, 255, 255, 0.1);
        backdrop-filter: blur(10px);
        padding: 2rem;
        border-radius: 15px;
        border: 1px solid rgba(255, 255, 255, 0.2);
        color: white;
    }

    .form-guidance h3 {
        margin-bottom: 1rem;
        color: #ffd700;
        border-bottom: 2px solid rgba(255, 215, 0, 0.3);
        padding-bottom: 0.5rem;
    }

    .form-guidance ul {
        list-style: none;
        padding: 0;
    }

    .form-guidance li {
        margin-bottom: 1rem;
        padding-left: 1.5rem;
        position: relative;
    }

    .form-guidance li:before {
        content: "💡";
        position: absolute;
        left: 0;
    }

    @media (max-width: 768px) {
        .service-form-container {
            padding: 1rem;
        }

        .service-type-options {
            flex-direction: column;
        }

        .form-actions {
            flex-direction: column;
        }

        .btn {
            width: 100%;
            justify-content: center;
        }
    }
    </style>
</@layout.base>
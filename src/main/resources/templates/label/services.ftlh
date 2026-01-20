<#ftl output_format="HTML" auto_esc=true>
<#assign title="Manage Services">
<#import "../layouts/base.ftlh" as layout>
<#import "../components/service-card.ftlh" as components>

<@layout.base>
    <div class="services-container">
        <div class="services-header">
            <h1>Manage Services</h1>
            <p>Create and manage services you offer to artists</p>
            <a href="${contextPath}/service/create" class="btn btn-primary">
                + Add New Service
            </a>
        </div>

        <#if services?? && services?size gt 0>
            <div class="services-grid">
                <#list services as service>
                    <@components.service_card service=service />
                </#list>
            </div>
        <#else>
            <div class="empty-state">
                <div class="empty-content">
                    <h3>No Services Yet</h3>
                    <p>Start by creating your first service that artists can subscribe to.</p>
                    <p class="hint">Common services include: Track Release, Music Production, Marketing, Distribution</p>
                    <a href="${contextPath}/service/create" class="btn btn-primary btn-large">
                        Create Your First Service
                    </a>
                </div>
            </div>
        </#if>

        <div class="services-info">
            <h3>Service Management Tips</h3>
            <div class="tips-grid">
                <div class="tip">
                    <h4>🎵 Track Release</h4>
                    <p>Essential service for artists to submit tracks for consideration. Usually free.</p>
                </div>
                <div class="tip">
                    <h4>💰 Premium Services</h4>
                    <p>Consider adding paid services like production, mixing, or marketing packages.</p>
                </div>
                <div class="tip">
                    <h4>📝 Clear Descriptions</h4>
                    <p>Clearly describe what artists get with each service to manage expectations.</p>
                </div>
            </div>
        </div>
    </div>
</@layout.base>

<style>
.services-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 3rem;
    color: white;
    flex-wrap: wrap;
    gap: 1rem;
}

.services-header h1 {
    margin: 0;
}

.services-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 2rem;
    margin-bottom: 3rem;
}

.empty-state {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 4rem;
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    margin-bottom: 3rem;
    text-align: center;
    color: white;
}

.empty-content h3 {
    margin-bottom: 1rem;
    font-size: 1.5rem;
}

.empty-content p {
    margin-bottom: 1rem;
    opacity: 0.9;
}

.hint {
    font-style: italic;
    font-size: 0.9rem;
    opacity: 0.7;
}

.btn-large {
    padding: 1rem 2rem;
    font-size: 1.1rem;
}

.services-info {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.services-info h3 {
    margin-bottom: 1.5rem;
    color: #333;
    text-align: center;
}

.tips-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1.5rem;
}

.tip {
    text-align: center;
    padding: 1.5rem;
}

.tip h4 {
    margin-bottom: 1rem;
    color: #667eea;
}

.tip p {
    color: #666;
    line-height: 1.5;
}

@media (max-width: 768px) {
    .services-header {
        flex-direction: column;
        text-align: center;
    }

    .services-grid {
        grid-template-columns: 1fr;
    }
}
</style>
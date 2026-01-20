<#ftl output_format="HTML" auto_esc=true>
<#assign title="Label Dashboard">
<#import "../layouts/base.ftlh" as layout>
<#import "../components/service-card.ftlh" as components>

<@layout.base>
    <div class="dashboard-header">
        <h1>Label Dashboard</h1>
        <p>Manage your label services and track submissions</p>
    </div>

    <div class="dashboard-stats">
        <div class="stat-card">
            <h3>${pendingSubmissionsCount!0}</h3>
            <p>Pending Submissions</p>
        </div>
        <div class="stat-card">
            <h3>${totalSubmissionsCount!0}</h3>
            <p>Total Submissions</p>
        </div>
        <div class="stat-card">
            <h3>${servicesCount!0}</h3>
            <p>Active Services</p>
        </div>
        <div class="stat-card">
            <h3>${approvalRate!0}%</h3>
            <p>Approval Rate</p>
        </div>
    </div>

    <div class="dashboard-actions">
        <a href="${contextPath}/submissions/pending" class="btn btn-primary">
            Review Submissions (${pendingSubmissionsCount!0})
        </a>
        <a href="${contextPath}/service/manage" class="btn btn-secondary">
            Manage Services
        </a>
        <a href="${contextPath}/profile/label" class="btn btn-outline">
            Label Profile
        </a>
    </div>

    <div class="dashboard-sections">
        <div class="section">
            <h2>Recent Submissions</h2>
            <#if recentSubmissions?? && recentSubmissions?size gt 0>
                <div class="submissions-list compact">
                    <#list recentSubmissions as submission>
                        <div class="submission-item compact ${submission.status?lower_case}">
                            <div class="submission-info">
                                <h4>${submission.trackTitle}</h4>
                                <p class="artist">By: ${submission.artistName!"Unknown Artist"}</p>
                                <p class="date">${submission.formattedSubmissionDate}</p>
                            </div>
                            <div class="submission-actions">
                                <a href="${contextPath}/submission/pending"
                                   class="btn btn-sm btn-primary">
                                    Review
                                </a>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="empty-state small">
                    <p>No recent submissions</p>
                </div>
            </#if>
        </div>

        <div class="section">
            <h2>Your Services</h2>
            <#if labelServices?? && labelServices?size gt 0>
                <div class="services-grid compact">
                    <#list labelServices as service>
                        <@components.service_card service=service showActions=false />
                    </#list>
                </div>
                <div class="text-center">
                    <a href="${contextPath}/service/manage" class="btn btn-outline">
                        View All Services
                    </a>
                </div>
            <#else>
                <div class="empty-state small">
                    <p>No services created yet</p>
                    <a href="${contextPath}/service/create" class="btn btn-primary">
                        Create First Service
                    </a>
                </div>
            </#if>
        </div>
    </div>
</@layout.base>

<style>
.dashboard-sections {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
    margin-top: 3rem;
}

.section {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.section h2 {
    margin-bottom: 1.5rem;
    color: #333;
    border-bottom: 2px solid #667eea;
    padding-bottom: 0.5rem;
}

.submissions-list.compact {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.submission-item.compact {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
    background: #f8f9fa;
    border-radius: 8px;
    border-left: 4px solid #667eea;
}

.submission-item.compact.pending {
    border-left-color: #ffa500;
}

.submission-info h4 {
    margin: 0 0 0.25rem 0;
    font-size: 1rem;
}

.artist {
    font-size: 0.9rem;
    color: #666;
    margin: 0;
}

.date {
    font-size: 0.8rem;
    color: #888;
    margin: 0;
}

.services-grid.compact {
    display: grid;
    gap: 1rem;
}

.empty-state.small {
    text-align: center;
    padding: 2rem;
    color: #666;
}

.empty-state.small p {
    margin-bottom: 1rem;
}

@media (max-width: 968px) {
    .dashboard-sections {
        grid-template-columns: 1fr;
    }
}
/* Добавь это в конец твоего CSS */

/* Исправление белого текста в карточках */
.section {
    color: #333 !important;
}

.section * {
    color: inherit !important;
}

.submission-item.compact {
    color: #333 !important;
}

.submission-item.compact * {
    color: inherit !important;
}

.submission-info h4 {
    color: #333 !important;
}

.artist {
    color: #666 !important;
}

.date {
    color: #888 !important;
}

.empty-state.small {
    color: #666 !important;
}

.empty-state.small p {
    color: #666 !important;
}

.submission-actions .btn {
    color: white !important;
}
</style>
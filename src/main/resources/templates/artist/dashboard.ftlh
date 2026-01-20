<#ftl output_format="HTML" auto_esc=true>
<#assign title="Artist Dashboard">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="dashboard-header">
        <h1>Artist Dashboard</h1>
        <p>Manage your music submissions and profile</p>
    </div>

    <div class="dashboard-stats">
        <div class="stat-card">
            <h3>${submissionsCount!0}</h3>
            <p>Total Submissions</p>
        </div>
        <div class="stat-card">
            <h3>${pendingSubmissions!0}</h3>
            <p>Pending Review</p>
        </div>
        <div class="stat-card">
            <h3>${approvedSubmissions!0}</h3>
            <p>Approved Tracks</p>
        </div>
    </div>

    <div class="dashboard-actions">
        <a href="${contextPath}/submission/create" class="btn btn-primary">
            Submit New Track
        </a>
        <a href="${contextPath}/submission/my" class="btn btn-secondary">
            View My Submissions
        </a>
        <a href="${contextPath}/profile/artist" class="btn btn-outline">
            Edit Profile
        </a>
    </div>

    <#if recentSubmissions?? && recentSubmissions?size gt 0>
        <div class="recent-submissions">
            <h2>Recent Submissions</h2>
            <div class="submissions-grid">
                <#list recentSubmissions as submission>
                    <#include "../components/submission-card.ftlh">
                </#list>
            </div>
        </div>
    </#if>
</@layout.base>

<style>
.dashboard-header {
    text-align: center;
    margin-bottom: 3rem;
    color: white;
}

.dashboard-stats {
    display: flex;
    gap: 2rem;
    justify-content: center;
    margin-bottom: 3rem;
    flex-wrap: wrap;
}

.stat-card {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 2rem;
    border-radius: 15px;
    text-align: center;
    color: white;
    min-width: 150px;
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.stat-card h3 {
    font-size: 2.5rem;
    margin-bottom: 0.5rem;
}

.dashboard-actions {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-bottom: 3rem;
    flex-wrap: wrap;
}

.btn-outline {
    background: transparent;
    color: white;
    border: 2px solid white;
}

.btn-outline:hover {
    background: white;
    color: #333;
}

.recent-submissions {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.submissions-grid {
    display: grid;
    gap: 1.5rem;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
}
</style>
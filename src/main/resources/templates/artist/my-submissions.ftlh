<#ftl output_format="HTML" auto_esc=true>
<#assign title="My Submissions">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="submissions-container">
        <div class="submissions-header">
            <h1>My Submissions</h1>
            <p>Track your submitted tracks and their status</p>
        </div>

        <#if submissions?? && submissions?size gt 0>
            <div class="submissions-list">
                <#list submissions as submission>
                    <div class="submission-item ${submission.status?lower_case}">
                        <div class="submission-header">
                            <h3>${submission.trackTitle}</h3>
                            <span class="status-badge status-${submission.status?lower_case}">
                                ${submission.status}
                            </span>
                        </div>

                        <div class="submission-details">
                            <p><strong>Label:</strong> ${submission.labelName!"Unknown Label"}</p>
                            <p><strong>Service:</strong> ${submission.serviceName!"Unknown Service"}</p>
                            <p><strong>Submitted:</strong> ${submission.formattedSubmissionDate}</p>

                            <p><strong>Reviewed:</strong> ${submission.formattedAdministeredDate}</p>

                            <#if submission.labelComment??>
                                <div class="label-comment">
                                    <strong>Label Feedback:</strong>
                                    <p>${submission.labelComment}</p>
                                </div>
                            </#if>
                        </div>

                        <div class="submission-actions">
                            <#if submission.status == "PENDING">
                                <span class="pending-note">Under review</span>
                            <#elseif submission.status == "APPROVED">
                                <span class="approved-note">🎉 Track approved!</span>
                            <#elseif submission.status == "REJECTED">
                                <span class="rejected-note">Track not accepted</span>
                            </#if>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="no-submissions">
                <div class="empty-state">
                    <h3>No Submissions Yet</h3>
                    <p>You haven't submitted any tracks to labels yet.</p>
                    <a href="${contextPath}/submission/create" class="btn btn-primary">
                        Submit Your First Track
                    </a>
                </div>
            </div>
        </#if>
    </div>
</@layout.base>

<style>
.submissions-container {
    max-width: 1000px;
    margin: 0 auto;
}

.submissions-header {
    text-align: center;
    margin-bottom: 3rem;
    color: white;
}

.submissions-list {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.submission-item {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    border-left: 5px solid #667eea;
}

.submission-item.pending {
    border-left-color: #ffa500;
}

.submission-item.approved {
    border-left-color: #4CAF50;
}

.submission-item.rejected {
    border-left-color: #f44336;
}

.submission-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 1rem;
}

.submission-header h3 {
    margin: 0;
    color: #333;
    flex: 1;
}

.status-badge {
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: bold;
    text-transform: uppercase;
}

.status-pending {
    background: #fff3cd;
    color: #856404;
}

.status-approved {
    background: #d4edda;
    color: #155724;
}

.status-rejected {
    background: #f8d7da;
    color: #721c24;
}

.submission-details {
    margin-bottom: 1rem;
}

.submission-details p {
    margin: 0.5rem 0;
    color: #666;
}

.label-comment {
    margin-top: 1rem;
    padding: 1rem;
    background: #f8f9fa;
    border-radius: 5px;
    border-left: 3px solid #667eea;
}

.label-comment strong {
    color: #333;
}

.label-comment p {
    margin: 0.5rem 0 0 0;
    font-style: italic;
}

.submission-actions {
    border-top: 1px solid #eee;
    padding-top: 1rem;
}

.pending-note {
    color: #ffa500;
    font-weight: 500;
}

.approved-note {
    color: #4CAF50;
    font-weight: 500;
}

.rejected-note {
    color: #f44336;
    font-weight: 500;
}

.no-submissions {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
}

.empty-state {
    text-align: center;
    color: white;
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 3rem;
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.empty-state h3 {
    margin-bottom: 1rem;
    font-size: 1.5rem;
}

.empty-state p {
    margin-bottom: 2rem;
    opacity: 0.9;
}
</style>
<#ftl output_format="HTML" auto_esc=true>
<div class="submission-card ${submission.status?lower_case}">
    <div class="card-header">
        <h4>${submission.trackTitle}</h4>
        <span class="status status-${submission.status?lower_case}">
            ${submission.status}
        </span>
    </div>

    <div class="card-body">
        <p class="label">To: ${submission.labelName!"Unknown Label"}</p>
        <p class="service">Service: ${submission.serviceName!"Unknown Service"}</p>
        <p class="date">Submitted: ${submission.formattedSubmissionDate}</p>

        <#if submission.labelComment??>
            <div class="comment">
                <strong>Feedback:</strong> ${submission.labelComment}
            </div>
        </#if>
    </div>
</div>

<style>
.submission-card {
    background: #f8f9fa;
    border-radius: 10px;
    padding: 1.5rem;
    border-left: 4px solid #667eea;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.submission-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.submission-card.pending {
    border-left-color: #ffa500;
}

.submission-card.approved {
    border-left-color: #4CAF50;
}

.submission-card.rejected {
    border-left-color: #f44336;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 1rem;
}

.card-header h4 {
    margin: 0;
    color: #333;
    flex: 1;
}

.status {
    padding: 0.2rem 0.6rem;
    border-radius: 15px;
    font-size: 0.7rem;
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

.card-body p {
    margin: 0.25rem 0;
    color: #666;
    font-size: 0.9rem;
}

.comment {
    margin-top: 0.5rem;
    padding: 0.5rem;
    background: white;
    border-radius: 5px;
    border-left: 3px solid #667eea;
    font-size: 0.85rem;
}

.comment strong {
    color: #333;
}
</style>
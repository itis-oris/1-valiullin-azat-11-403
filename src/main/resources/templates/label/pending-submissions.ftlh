<#ftl output_format="HTML" auto_esc=true>
<#assign title="Pending Submissions">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="submissions-container">
        <div class="submissions-header">
            <h1>Pending Submissions</h1>
            <p>Review and manage track submissions from artists</p>
            <div class="submission-stats">
                <span class="stat">${submissions?size} submissions awaiting review</span>
            </div>
        </div>

        <#if submissions?? && submissions?size gt 0>
            <div class="submissions-list">
                <#list submissions as submission>
                    <div class="submission-review-card">
                        <div class="submission-main">
                            <div class="track-info">
                                <h3>${submission.trackTitle}</h3>
                                <div class="artist-info">
                                    <strong>Artist:</strong> ${submission.artistName!"Unknown Artist"}
                                </div>
                                <div class="service-info">
                                    <strong>Service:</strong> ${submission.serviceName!"Unknown Service"}
                                </div>
                                <div class="submission-date">
                                    Submitted: ${submission.formattedSubmissionDate}
                                </div>
                            </div>

                            <div class="track-actions">
                                <#if submission.trackFileUrl??>
                                    <audio controls class="audio-player">
                                        <source src="${contextPath}/uploads/${submission.trackFileUrl}" type="audio/mpeg">
                                        Your browser does not support the audio element.
                                    </audio>
                                </#if>
                            </div>
                        </div>

                        <div class="review-form">
                            <form action="${contextPath}/submission/review" method="post" class="review-form-inline">
                                <input type="hidden" name="submissionId" value="${submission.id}">

                                <div class="form-group">
                                    <label for="comment-${submission.id}">Feedback to Artist</label>
                                    <textarea id="comment-${submission.id}" name="comment" rows="3"
                                              placeholder="Provide constructive feedback to the artist..."></textarea>
                                </div>

                                <div class="decision-buttons">
                                    <button type="submit" name="decision" value="APPROVE"
                                            class="btn btn-success" onclick="return confirm('Approve this submission?')">
                                        ✅ Approve
                                    </button>
                                    <button type="submit" name="decision" value="REJECT"
                                            class="btn btn-danger" onclick="return confirm('Reject this submission?')">
                                        ❌ Reject
                                    </button>
                                    <button type="button" class="btn btn-secondary"
                                            onclick="skipSubmission(${submission.id})">
                                        ⏭️ Skip
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="empty-state">
                <div class="empty-content">
                    <h3>No Pending Submissions</h3>
                    <p>All submissions have been reviewed. Check back later for new submissions.</p>
                    <div class="success-icon">🎉</div>
                    <a href="${contextPath}/label/dashboard" class="btn btn-primary">
                        Return to Dashboard
                    </a>
                </div>
            </div>
        </#if>
    </div>

    <script>
    function skipSubmission(submissionId) {
        // Simple implementation - just reload the page for now
        // In a real app, you might mark it as "viewed" or implement proper skipping
        alert('Submission skipped. It will remain in the pending list.');
    }

    // Enhance audio player experience
    document.addEventListener('DOMContentLoaded', function() {
        const audioPlayers = document.querySelectorAll('.audio-player');
        audioPlayers.forEach(player => {
            player.addEventListener('play', function() {
                // Pause other players when one starts playing
                audioPlayers.forEach(otherPlayer => {
                    if (otherPlayer !== player && !otherPlayer.paused) {
                        otherPlayer.pause();
                    }
                });
            });
        });
    });
    </script>
</@layout.base>

<style>
.submissions-header {
    text-align: center;
    margin-bottom: 3rem;
    color: white;
}

.submission-stats {
    margin-top: 1rem;
}

.stat {
    background: rgba(255, 255, 255, 0.2);
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-weight: 500;
    color: white; /* Явно задаем белый цвет */
}

.submissions-list {
    display: flex;
    flex-direction: column;
    gap: 2rem;
}

.submission-review-card {
    background: white;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    overflow: hidden;
    color: #333; /* Основной цвет текста для карточки */
}

.submission-main {
    display: grid;
    grid-template-columns: 1fr auto;
    gap: 2rem;
    padding: 2rem;
    border-bottom: 1px solid #eee;
    align-items: start;
}

.track-info h3 {
    margin: 0 0 1rem 0;
    color: #333;
    font-size: 1.3rem;
}

.artist-info, .service-info, .submission-date {
    margin: 0.5rem 0;
    color: #666;
}

.track-actions {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    align-items: center;
    min-width: 200px;
}

.audio-player {
    width: 100%;
    height: 40px;
}

.review-form {
    padding: 2rem;
    background: #f8f9fa;
    color: #333; /* Явно задаем цвет текста */
}

.review-form-inline {
    display: grid;
    grid-template-columns: 1fr auto;
    gap: 2rem;
    align-items: end;
    color: #333; /* Явно задаем цвет текста */
}

.review-form-inline .form-group {
    margin: 0;
    color: #333; /* Явно задаем цвет текста */
}

.review-form-inline label {
    color: #333; /* Цвет для меток */
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 500;
}

.review-form-inline textarea {
    width: 100%;
    resize: vertical;
    min-height: 80px;
    color: #333; /* Цвет текста в textarea */
    background: white;
    border: 1px solid #ddd;
    padding: 0.5rem;
    border-radius: 4px;
}

.decision-buttons {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    min-width: 150px;
}

/* Стили для кнопок */
.btn {
    padding: 0.5rem 1rem;
    border-radius: 4px;
    text-decoration: none;
    font-weight: 500;
    border: none;
    cursor: pointer;
    transition: background-color 0.2s;
    text-align: center;
}

.btn-success {
    background: #4CAF50;
    color: white;
}

.btn-success:hover {
    background: #45a049;
}

.btn-danger {
    background: #f44336;
    color: white;
}

.btn-danger:hover {
    background: #da190b;
}

.btn-secondary {
    background: #6c757d;
    color: white;
}

.btn-secondary:hover {
    background: #5a6268;
}

.btn-outline {
    background: transparent;
    color: #007bff;
    border: 1px solid #007bff;
}

.btn-outline:hover {
    background: #007bff;
    color: white;
}

.empty-state .success-icon {
    font-size: 3rem;
    margin: 1rem 0;
}

@media (max-width: 968px) {
    .submission-main {
        grid-template-columns: 1fr;
        text-align: center;
    }

    .review-form-inline {
        grid-template-columns: 1fr;
    }

    .decision-buttons {
        flex-direction: row;
        justify-content: center;
    }
}
</style>
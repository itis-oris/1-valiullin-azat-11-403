package com.itis403.app.service;

import com.itis403.app.dao.ArtistProfileDao;
import com.itis403.app.dao.SubmissionDao;
import com.itis403.app.dao.ServiceDao;
import com.itis403.app.model.Submission;
import com.itis403.app.model.SubmissionStatus;
import java.util.List;
import java.util.Optional;

public class SubmissionService {

    private final SubmissionDao submissionDao;
    private final ServiceDao serviceDao;
    private final ArtistProfileDao artistProfileDao;

    public SubmissionService(SubmissionDao submissionDao, ServiceDao serviceDao, ArtistProfileDao artistProfileDao) {
        this.submissionDao = submissionDao;
        this.serviceDao = serviceDao;
        this.artistProfileDao = artistProfileDao;
    }

    public Long getArtistProfileId(Long userId) {
        System.out.println("=== SubmissionService.getArtistProfileId() ===");
        System.out.println("📝 Looking for artist profile for user ID: " + userId);

        try {
            Optional<Long> profileId = artistProfileDao.findProfileIdByUserId(userId);

            if (profileId.isPresent()) {
                System.out.println("✅ Found artist profile ID: " + profileId.get());
                return profileId.get();
            } else {
                System.out.println("❌ Artist profile not found for user: " + userId);
                throw new RuntimeException("Artist profile not found for user: " + userId);
            }
        } catch (Exception e) {
            System.err.println("💥 ERROR in getArtistProfileId: " + e.getMessage());
            throw e;
        }
    }

    public List<Submission> getSubmissionsByArtist(Long artistId) {
        return submissionDao.findByArtistId(artistId);
    }

    public List<Submission> getPendingSubmissionsByLabel(Long labelId) {
        return submissionDao.findPendingByLabelId(labelId);
    }

    public List<Submission> getRecentSubmissionsByArtist(Long artistId, int limit) {
        List<Submission> submissions = submissionDao.findByArtistId(artistId);
        return submissions.stream().limit(limit).toList();
    }

    public List<Submission> getRecentSubmissionsByLabel(Long labelId, int limit) {
        List<Submission> submissions = submissionDao.findPendingByLabelId(labelId);
        return submissions.stream().limit(limit).toList();
    }

    public void createSubmission(Long artistId, Long serviceId, String trackTitle, String trackFileUrl) {
        Submission submission = new Submission(artistId, serviceId, trackTitle, trackFileUrl);
        submissionDao.save(submission);
    }

    public void approveSubmission(Long submissionId, String comment) {
        submissionDao.updateStatus(submissionId, SubmissionStatus.APPROVED, comment);
    }

    public void rejectSubmission(Long submissionId, String comment) {
        submissionDao.updateStatus(submissionId, SubmissionStatus.REJECTED, comment);
    }

    public int getSubmissionsCountByArtist(Long artistId) {
        return submissionDao.countByArtistId(artistId);
    }

    public int getPendingSubmissionsCountByArtist(Long artistId) {
        return submissionDao.countPendingByArtistId(artistId);
    }

    public int getApprovedSubmissionsCountByArtist(Long artistId) {
        return submissionDao.countApprovedByArtistId(artistId);
    }

    public int getPendingSubmissionsCountByLabel(Long labelId) {
        List<Submission> submissions = submissionDao.findPendingByLabelId(labelId);
        return submissions.size();
    }

    public int getTotalSubmissionsCountByLabel(Long labelId) {
        // This should be implemented in SubmissionDao
        // For now, estimate from pending submissions
        return getPendingSubmissionsCountByLabel(labelId) * 2;
    }

    public double getApprovalRateByLabel(Long labelId) {
        // Simple calculation for demo
        int total = getTotalSubmissionsCountByLabel(labelId);
        int approved = total / 2; // Demo value
        return total > 0 ? (approved * 100.0 / total) : 0;
    }

    public Submission getSubmissionById(Long submissionId) {
        return submissionDao.findById(submissionId).orElse(null);
    }
}